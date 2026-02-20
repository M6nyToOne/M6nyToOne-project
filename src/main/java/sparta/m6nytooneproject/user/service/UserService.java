package sparta.m6nytooneproject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.config.PasswordEncoder;
import sparta.m6nytooneproject.global.dto.LoginRequest;
import sparta.m6nytooneproject.global.dto.SessionUser;
import sparta.m6nytooneproject.user.dto.*;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 디폴트가 읽기모드
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 슈퍼 관리자 계정
    User superAdmin = new User("이승현", "${SUPER_ADMIN_EMAIL}", "${SUPER_ADMIN_PASSWORD}",
                     "${SUPER_ADMIN_PHONE-NUMBER}",UserRole.SUPER_ADMIN);

    @Transactional // 쓰기모드
    public UserResponseDto createUser(UserRequestDto requestDto) {
        // 회원가입 시 이메일 중복 확인
        boolean existence = userRepository.existsByEmail(requestDto.getEmail());
        if (existence) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getPhoneNumber(),
                requestDto.getUserRole()
        );
        User savedUser = userRepository.save(user);
        if (requestDto.getUserRole().equals(UserRole.CUSTOMER)) {
            savedUser.setSignupStatus(SignupStatus.ACTIVE);
            return new UserResponseDto(savedUser);
        }
        savedUser.setSignupStatus(SignupStatus.PENDING);
        return new UserResponseDto(savedUser);
        // 슈퍼 관리자가 승인해야 됨.
    }
    // 로그인
    public SessionUser login(LoginRequest request) {
        // 이메일이 유효한지
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalStateException("이미 로그인되어있는 유저입니다.")
        );
        // 비밀번호가 일치하는지
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        // 회원가입 상태가 활성되지 않았다면 예외
        if (!user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
            switch (user.getSignupStatus()) {
                case PENDING -> throw new IllegalStateException("회원가입 승인 대기중입니다.");
                case REJECTED -> throw new IllegalStateException("회원가입 신청이 거부되었습니다.");
                case SUSPEND -> throw new IllegalStateException("계정이 정지되었습니다.");
                case INACTIVE -> throw new IllegalStateException("계정이 비활성화 상태입니다.");
            }
        }
        // 활성상태라면 로그인
        return new SessionUser(user);
    }

    // 슈퍼 관리자가 승인대기중인 관리자 전체조회
    public Page<UserResponseDto> getPendingUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findByRoleInAndSignupStatus(
                List.of(UserRole.SUPER_ADMIN, UserRole.OPER_ADMIN, UserRole.MARKET_ADMIN, UserRole.CS_ADMIN),
                SignupStatus.PENDING,
                pageable
                ).map(UserResponseDto::new);
    }

    // 슈퍼 관리자가 승인대기중인 관리자 승인(업데이트) / 각종 상태 변경
    @Transactional
    public UpdateUserStatusResponseDto updatePendingUser(Long userId, UpdateUserStatusRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        // 슈퍼 관리자가 승인대기 상태를 활성/거부 상태로 업데이트
        // 거부 사유가 null이 아니면 거부 상태로 변경
        if (requestDto.getRejectMessage() != null) {
            user.setSignupStatus(SignupStatus.REJECTED);
            return new UpdateUserStatusResponseDto(user);
        }
        // null이라면 활성 상태로 변경
        user.setSignupStatus(SignupStatus.ACTIVE);
        return new UpdateUserStatusResponseDto(user);
    }

    // 등록된 관리자 전체 조회
    public Page<UserResponseDto> getRegisteredUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findByRoleNotAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE, pageable);
        return users.map(UserResponseDto::new);
    }

    // 등록된 관리자 상제 조회 (단 건 조회)
    public UserResponseDto getOneRegisteredUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        if (user.getRole().equals(UserRole.CUSTOMER)) {
            throw new IllegalStateException("관리자 계정이 아닙니다.");
        }
        if (user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
            throw new IllegalStateException("승인된 계정이 아닙니다.");
        }
        return new UserResponseDto(user);
    }

    // 등록된 관리자 정보 수정
    @Transactional
    public UpdateUserInfoResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        user.updateUserInfo(
                requestDto.getUserName(),
                requestDto.getEmail(),
                requestDto.getPhoneNumber()
        );
        return new UpdateUserInfoResponseDto(user);
    }

    // 등록된 관리자 역할 변경
    @Transactional
    public UpdateRegisteredUserResponseDto updateRegisteredUser(Long userId, UpdateRegisteredRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        if (requestDto.getUserRole().equals(user.getRole())) {
            throw new IllegalStateException("이미 해당 역할입니다.");
        }
        user.updateUserRole(requestDto.getUserRole());
        return new UpdateRegisteredUserResponseDto(user);
    }


    // 내 프로필 조회
    public GetUserResponseDto getMyInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        return new GetUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        boolean existence = userRepository.existsById(userId);
        if (!existence) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        userRepository.deleteById(userId);
    }
}

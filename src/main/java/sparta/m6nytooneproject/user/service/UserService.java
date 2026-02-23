package sparta.m6nytooneproject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.config.PasswordEncoder;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.LoginRequestDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.global.exception.user.UserRoleNotMatchException;
import sparta.m6nytooneproject.global.exception.common.SessionNotActiveException;
import sparta.m6nytooneproject.global.exception.common.UnAuthorizedException;
import sparta.m6nytooneproject.global.exception.user.*;
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

    // ===== 3. 관리자 정보 관리 =====

    // 슈퍼 관리자 계정
    User superAdmin = new User("이승현", "${SUPER_ADMIN_EMAIL}", "${SUPER_ADMIN_PASSWORD}",
                     "${SUPER_ADMIN_PHONE-NUMBER}",UserRole.SUPER_ADMIN);

    @Transactional // 쓰기모드
    public UserResponseDto createUser(UserRequestDto requestDto) {
        // 회원가입 시 이메일 중복 확인
        boolean existence = userRepository.existsByEmail(requestDto.getEmail());
        if (existence) {
            throw new AlreadyExistingEmailException("이미 존재하는 이메일입니다.");
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
    public SessionUserDto login(LoginRequestDto request) {
        // 이메일이 유효한지
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new AlreadyLoginUserException("이미 로그인되어있는 유저입니다.")
        );
        // 비밀번호가 일치하는지
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnmatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
        // 회원가입 상태가 활성되지 않았다면 예외
        if (!user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
            switch (user.getSignupStatus()) {
                case PENDING -> throw new SessionNotActiveException("회원가입 승인 대기중입니다.");
                case REJECTED -> throw new SessionNotActiveException("회원가입 신청이 거부되었습니다.");
                case SUSPEND -> throw new SessionNotActiveException("계정이 정지되었습니다.");
                case INACTIVE -> throw new SessionNotActiveException("계정이 비활성화 상태입니다.");
            }
        }
        // 활성상태라면 로그인
        return new SessionUserDto(user);
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
    public UpdateUserStatusResponseDto updatePendingUser(Long userId, UpdateUserStatusRequestDto request) {
        User user = getUserById(userId);
        // 슈퍼 관리자가 승인대기 상태를 활성/거부 상태로 업데이트
        // 거부 사유가 null이 아니면 거부 상태로 변경
        if (request.getRejectMessage() != null) {
            user.setSignupStatus(SignupStatus.REJECTED);
            return new UpdateUserStatusResponseDto(user);
        }
        // null이라면 활성 상태로 변경
        user.setSignupStatus(SignupStatus.ACTIVE);
        return new UpdateUserStatusResponseDto(user);
    }

    // 등록된 관리자 전체 조회
    public Page<UserResponseDto> getRegisteredUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findByRoleNotAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE, pageable);
        return users.map(UserResponseDto::new);
    }

    // 등록된 관리자 상제 조회 (단 건 조회)
    public UserResponseDto getOneRegisteredUser(Long userId) {
        User user = getUserById(userId);
        if (user.getRole().equals(UserRole.CUSTOMER)) {
            throw new UnAuthorizedException("관리자 계정이 아닙니다.");
        }
        if (user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
            throw new UnAuthorizedException("승인된 계정이 아닙니다.");
        }
        return new UserResponseDto(user);
    }

    // 등록된 관리자 정보 수정
    // TODO: 슈퍼관리자가 다른 관리자 수정하는거니까 비번 검증 필요없나?
    @Transactional
    public UpdateUserInfoResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto request) {
        User user = getUserById(userId);
        user.updateUserInfo(
                request.getUserName(),
                request.getEmail(),
                request.getPhoneNumber()
        );
        return new UpdateUserInfoResponseDto(user);
    }

    // 등록된 관리자 역할 변경
    @Transactional
    public UpdateRegisteredUserResponseDto updateRegisteredUser(Long userId, UpdateRegisteredRequestDto request) {
        User user = getUserById(userId);
        if (request.getUserRole().equals(user.getRole())) {
            throw new AlreadySameRoleException("이미 해당 역할입니다.");
        }
        user.updateUserRole(request.getUserRole());
        return new UpdateRegisteredUserResponseDto(user);
    }

    // 등록된 관리자 삭제

    @Transactional
    public void deleteUser(Long userId) {
        boolean existence = userRepository.existsById(userId);
        if (!existence) {
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
        userRepository.deleteById(userId);
    }
    // 해당 유저id가 존재하는지 확인하는 메서드

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
    }

    // 내 프로필 조회
    public GetUserResponseDto getMyInfo(Long userId) {
        User user = getUserById(userId);
        return new GetUserResponseDto(user);
    }

    // 내 프로필 수정 (이름, 이메일, 전화번호), 비번이 일치해야 수정할 수 있게.
    @Transactional
    public UpdateUserInfoResponseDto updateMyInfo(Long userId, UpdateUserInfoRequestDto request) {
        User user = getUserById(userId);
        user.updateUserInfo(request.getUserName(), request.getEmail(), request.getPhoneNumber());
        return new UpdateUserInfoResponseDto(user);
    }

    // TODO: 기존비밀번호와 동일했을때의 에러 수정 필요
    @Transactional
    public void changeMyPassword(Long userId, UpdateMyPasswordRequestDto request) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UnmatchPasswordException("현재 비밀번호가 일치하지 않습니다.");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalStateException("기존 비밀번호와 다르게 설정해주세요.");
        }
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // ===== 4. 고객 정보 관리 =====

    // 고객 전체조회 (페이징)
    public Page<GetAllCustomerResponseDto> getAllCustomer(int page, int size) {
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findByRoleAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE, pageable);
        return users.map(GetAllCustomerResponseDto::new);
    }

    // 내 비밀변호 변경

    // 고객이 맞는지 검증하는 메서드
    public void checkValidCustomer(UserRole role){
        if(!role.equals(UserRole.CUSTOMER)){
            throw new UserRoleNotMatchException("권한이 올바르지 않습니다.");
        }
    }

    //삭제, 수정 등 작업을 요청할때 요청한 유저와 그것을 작성한 유저가 동일한지 확인하는 서비스
    public void validateRequesterIsOwner(Long requesterId, Long ownerId){
        if(!requesterId.equals(ownerId)){
            throw new UnAuthorizedException("권한이 없습니다.");
        }
    }
}

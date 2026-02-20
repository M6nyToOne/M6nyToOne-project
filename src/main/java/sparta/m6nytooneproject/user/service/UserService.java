package sparta.m6nytooneproject.user.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.config.PasswordEncoder;
import sparta.m6nytooneproject.global.dto.LoginRequest;
import sparta.m6nytooneproject.global.dto.SessionUser;
import sparta.m6nytooneproject.user.dto.UpdateUserRequestDto;
import sparta.m6nytooneproject.user.dto.UpdateUserResponseDto;
import sparta.m6nytooneproject.user.dto.UserRequestDto;
import sparta.m6nytooneproject.user.dto.UserResponseDto;
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
        // 고객이면 회원가입 바로 시켜주기
        if (requestDto.getUserRole().equals(UserRole.CUSTOMER)) {
            User savedUser = userRepository.save(user);
            return new UserResponseDto(savedUser);
        }
        // 승인 대기
        User savedUser = userRepository.save(user);
        savedUser.setSignupStatus(SignupStatus.PENDING);
        return new UserResponseDto(savedUser);

        // 슈퍼 관리자가 승인해야 됨.
    }

    // 페이징 메서드
    public Page<User> getPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    // 승인 대기중인 관리자 조회
    public List<UserResponseDto> getPendingUsers(Page<User> userPage) {
        return userPage.stream().map(UserResponseDto::new).toList();
    }

    // 슈퍼 관리자가 승인대기중인 관리자 승인
    @Transactional
    public UpdateUserResponseDto updatePendingUser(Long userId, UpdateUserRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        user.updateLoginStatus(requestDto.getSignupStatus());
        return new UpdateUserResponseDto(user);
    }

    // 로그인
    public SessionUser login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalStateException("이미 로그인되어있는 유저입니다.")
        );
        if (!user.getSignupStatus().equals(SignupStatus.ACTIVE)) {
            throw new IllegalStateException("활성화되지 않는 유저입니다.");
        }
        return new SessionUser(user);
    }
}

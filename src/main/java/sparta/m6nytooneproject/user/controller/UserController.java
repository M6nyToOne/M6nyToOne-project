package sparta.m6nytooneproject.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.LoginRequest;
import sparta.m6nytooneproject.global.dto.SessionUser;
import sparta.m6nytooneproject.user.dto.*;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;
import sparta.m6nytooneproject.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 신규 관리자 회원가입 (회원가입시 승인대기 상태일 것 -> 슈퍼 관리자가 승인해줘야 로그인 가능)
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(requestDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        SessionUser sessionUser = userService.login(request);
        session.setAttribute("login_user", sessionUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @SessionAttribute(name = "login_user", required = false) SessionUser sessionUser,
            HttpSession session
    ) {
        // 세션유저가 없다면 잘못된 요청 에러 반환
        if (sessionUser == null) {
            return ResponseEntity.badRequest().build();
        }
        // 있다면 세션 무효화 후 NO_CONTENT 상태 반환
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 슈퍼 관리자가 신규 회원가입한(승인대기중인) 다른 관리자 전체조회
    @GetMapping("/pendings")
    public ResponseEntity<Page<UserResponseDto>> getPendingUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getPendingUsers(page, size));
    }

    // 슈퍼 관리자가 승인 대기중인 관리자 상태 변경 (상태 업데이트) / 각 종 상태 변경
    @PatchMapping("/pendings/{userId}")
    public ResponseEntity<UpdateUserStatusResponseDto> updatePendingUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePendingUser(userId, requestDto));
    }

    // 슈퍼 관리자가 등록된 전체 관리자 조회
    @GetMapping("/registered")
    public ResponseEntity<Page<UserResponseDto>> getRegisteredUsers(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getRegisteredUsers(page, size));

    }

    // 슈퍼 관리자가 등록된 관리자 상세 조회 (단 건 조회)
    @GetMapping("/registered/{userId}")
    public ResponseEntity<UserResponseDto> getOneRegisteredUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOneRegisteredUser(userId));
    }

    // 슈퍼관리자가 등록된 관리자들 정보 수정 (업데이트) - 이름, 이메일, 전화번호 수정 가능
    @PatchMapping("/registered/{userId}")
    public ResponseEntity<UpdateUserInfoResponseDto> updateUserInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserInfoRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserInfo(userId, requestDto));
    }

    // 슈퍼 관리자가 다른 관리자의 역할 변경 (업데이트)
    @PatchMapping("/registered/{userId}")
    public ResponseEntity<UpdateRegisteredUserResponseDto> updateRegisteredUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRegisteredRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateRegisteredUser(userId, requestDto));
    }

    // 슈퍼 관리자가 특정 관리자 삭제(탈퇴)
    @PatchMapping("/registered/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            UserRole userRole
    ) {
        // 고객 계정을 함부로 삭제 하면 안 됨..아 필요한가 이거?
        if (userRole.equals(UserRole.CUSTOMER)) {
            throw new IllegalStateException("해당 계정은 고객 계정입니다.");
        }
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 내 프로필 조회 (고객 유저)
    @GetMapping("/me")
    public ResponseEntity<GetUserResponseDto> getMyInfo(HttpSession session) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("login_user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.getMyInfo(sessionUser.getId()));
    }

    // 내 프로필 수정 (고객 유저)

    // 내 비밀번호 변경 (고객 유저)

}

package sparta.m6nytooneproject.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.LoginRequest;
import sparta.m6nytooneproject.global.dto.SessionUser;
import sparta.m6nytooneproject.user.dto.UpdateUserRequestDto;
import sparta.m6nytooneproject.user.dto.UpdateUserResponseDto;
import sparta.m6nytooneproject.user.dto.UserRequestDto;
import sparta.m6nytooneproject.user.dto.UserResponseDto;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.service.UserService;

import java.awt.*;
import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 신규 관리자 회원가입 (회원가입시 승인대기 상태일 것 -> 슈퍼 관리자가 승인해줘야 로그인 가능)
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(requestDto));
    }

    // 슈퍼 관리자가 회원가입한(승인대기중인) 다른 관리자 조회
    @GetMapping("/pendings")
    public ResponseEntity<List<UserResponseDto>> getPendingUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<User> userPage= userService.getPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getPendingUsers(userPage));
    }

    // 슈퍼 관리자가 승인 대기중인 관리자 업데이트 (승인)
    @PatchMapping("/pendings/{userId}")
    public ResponseEntity<UpdateUserResponseDto> updatePendingUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePendingUser(userId, requestDto));
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
}

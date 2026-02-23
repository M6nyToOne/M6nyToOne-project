package sparta.m6nytooneproject.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.LoginRequestDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.user.dto.*;
import sparta.m6nytooneproject.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ===== 3. 관리자 정보 관리 =====

    // 신규 관리자 회원가입 (회원가입시 승인대기 상태일 것 -> 슈퍼 관리자가 승인해줘야 로그인 가능)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(userService.createUser(requestDto)));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Void>> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(
            @SessionAttribute(name = AuthConstants.LOGIN_USER, required = false) SessionUserDto sessionUser,
            HttpSession session
    ) {
        // 세션유저가 없다면 잘못된 요청 에러 반환
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        // 있다면 세션 무효화 후 NO_CONTENT 상태 반환
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }

    // 슈퍼 관리자가 신규 회원가입한(승인대기중인) 다른 관리자 전체조회
    @GetMapping("/pendings")
    public ResponseEntity<ApiResponseDto<Page<UserResponseDto>>> getPendingUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getPendingUsers(page, size, sessionUser)));
    }

    // 슈퍼 관리자가 승인 대기중인 관리자 상태 변경 (상태 업데이트) / 각 종 상태 변경
    @PatchMapping("/pendings/{userId}")
    public ResponseEntity<ApiResponseDto<UpdateUserStatusResponseDto>> updatePendingUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updatePendingUser(userId, request, sessionUser)));
    }

    // 슈퍼 관리자가 등록된 전체 관리자 조회
    @GetMapping("/registered")
    public ResponseEntity<ApiResponseDto<Page<UserResponseDto>>> getRegisteredUsers(
            @RequestParam int page,
            @RequestParam int size,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getRegisteredUsers(page, size, sessionUser)));

    }

    // 슈퍼 관리자가 등록된 관리자 상세 조회 (단 건 조회)
    @GetMapping("/registered/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getOneRegisteredUser(
            @PathVariable Long userId,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getOneRegisteredUser(userId,sessionUser)));
    }

    // 슈퍼관리자가 등록된 관리자들 정보 수정 (업데이트) - 이름, 이메일, 전화번호 수정 가능
    @PatchMapping("/registered/{userId}/info")
    public ResponseEntity<ApiResponseDto<UpdateUserInfoResponseDto>> updateUserInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserInfoRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateUserInfo(userId, request, sessionUser)));
    }

    // 슈퍼 관리자가 다른 관리자의 역할 변경 (업데이트)
    @PatchMapping("/registered/{userId}/status")
    public ResponseEntity<ApiResponseDto<UpdateRegisteredUserResponseDto>> updateRegisteredUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRegisteredRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateRegisteredUser(userId, request, sessionUser)));
    }

    // 슈퍼 관리자가 특정 관리자 삭제(탈퇴)
    @DeleteMapping("/registered/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(
            @PathVariable Long userId,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        userService.deleteUser(userId, sessionUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }

    // 내 프로필 조회 (관리자 자신)
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<GetUserResponseDto>> getMyInfo(
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(ApiResponseDto.success(userService.getMyInfo(sessionUser.getId())));
    }

    // 내 프로필 수정 (관리자 자신)
    @PatchMapping("/me/update")
    public ResponseEntity<ApiResponseDto<UpdateUserInfoResponseDto>> updateMyInfo(
            @Valid @RequestBody UpdateUserInfoRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateMyInfo(sessionUser.getId(), request)));
    }

    // 내 비밀번호 변경 (관리자 자신)
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponseDto<UpdateMyPasswordResponseDto>> updateMyPassword(
            @Valid @RequestBody UpdateMyPasswordRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        userService.changeMyPassword(sessionUser.getId(), request);
        return ResponseEntity.ok(ApiResponseDto.successWithNoContent());
    }

    // ===== 4. 고객 정보 관리 ======

    // 플랫폼 이용하는 모든 고객 조회 (페이징)
    @GetMapping("/customers")
    public ResponseEntity<ApiResponseDto<Page<GetAllCustomerResponseDto>>> getAllCustomer(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getAllCustomer(page, size, sessionUser)));
    }

    // 고객 정보 상세 조회
    @GetMapping("/customers/{userId}")
    public ResponseEntity<ApiResponseDto<GetOneCustomerResponseDto>> getOneCustomer(
            @PathVariable Long userId,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getOneCustomer(userId, sessionUser)));
    }

    // 고객 정보 수정
    @PatchMapping("/customers/{userId}")
    public ResponseEntity<ApiResponseDto<UpdateCustomerInfoResponseDto>> updateCustomerInfo(
        @PathVariable Long userId,
        @RequestBody UpdateUserInfoRequestDto request,
        @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateCustomer(userId, request, sessionUser)));
    }

    // 고객 상태 변경
    @PatchMapping("/customers/{userId}/status")
    public ResponseEntity<ApiResponseDto<UpdateCustomerInfoResponseDto>> updateCustomerStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateCustomerStatusRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateCustomerStatus(userId, request, sessionUser)));
    }

    // 고객 삭제
    @DeleteMapping("/customers/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCustomer(
            @PathVariable Long userId,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        userService.deleteCustomer(userId, sessionUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}

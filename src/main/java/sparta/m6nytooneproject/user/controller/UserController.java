package sparta.m6nytooneproject.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.LoginRequestDto;
import sparta.m6nytooneproject.security.CustomUserDetails;
import sparta.m6nytooneproject.user.dto.*;
import sparta.m6nytooneproject.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 신규 관리자 회원가입
     * 회원가입 시 승인대기 상태 -> 슈퍼 관리자가 승인 후 로그인 가능
     *
     * @param requestDto 회원가입 요청 정보
     * @return 생성된 유저 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(userService.createUser(requestDto)));
    }

    /**
     * 회원 로그인
     *
     * @param request 로그인 아이디 비밀번호 정보
     * @return NO_CONTENT
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Void>> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }

    /**
     * 회원 로그아웃
     * 프론트에서 토큰을 파기하면 로그아웃이기 때문에 동작하는 로직은 없지만 session 방식 채용시 필요
     *
     * @return NO_CONTENT
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }

    /**
     * 승인 대기중인 관리자 회원 목록 조회
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param page - 페이지
     * @param size - 페이지 당 데이터 크기
     * @param userDetails - jwt token
     *
     * @return 회원 정보 pagination
     */
    @GetMapping("/pendings")
    public ResponseEntity<ApiResponseDto<Page<UserResponseDto>>> getPendingUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getPendingUsers(page, size, userDetails)));
    }

    /**
     * 승인 대기중인 관리자 승인으로 변경
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 변경할 유저 ID
     * @param request - 변경 상태, 거절시 거절사유 포함
     * @param userDetails - jwt token
     *
     * @return 변경된 유저 정보
     */
    @PatchMapping("/pendings/{userId}")
    @PreAuthorize("hasRole('SUPER')")
    public ResponseEntity<ApiResponseDto<UpdateUserStatusResponseDto>> updatePendingUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequestDto request
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updatePendingUser(userId, request)));
    }

    /**
     * 승인된 관리자만 조회
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param page - 페이지
     * @param size - 페이지 당 데이터 크기
     * @param userDetails - jwt token
     *
     * @return 승인된 관리자 정보 pagination
     */
    @GetMapping("/registered")
    public ResponseEntity<ApiResponseDto<Page<UserResponseDto>>> getRegisteredUsers(
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getRegisteredUsers(page, size, userDetails)));

    }

    /**
     * 승인된 관리자 상세 조회
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 조회할 유저 정보
     * @param userDetails - jwt token
     *
     * @return 상세 유저 정보
     */
    @GetMapping("/registered/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getOneRegisteredUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getOneRegisteredUser(userId,userDetails)));
    }

    /**
     * 승인된 관리자 정보 수정
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 수정할 유저 정보
     * @param request - 수정할 정보 [이름, 이메일, 전화번호 수정 가능]
     * @param userDetails - jwt token
     *
     * @return 수정된 유저 정보
     */
    @PatchMapping("/registered/{userId}/info")
    public ResponseEntity<ApiResponseDto<UpdateUserInfoResponseDto>> updateUserInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserInfoRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateUserInfo(userId, request, userDetails)));
    }

    /**
     * 승인된 관리자 role 수정
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 수정할 유저 정보
     * @param request - 수정할 role
     * @param userDetails - jwt token
     *
     * @return 수정된 유저 정보
     */
    @PatchMapping("/registered/{userId}/status")
    public ResponseEntity<ApiResponseDto<UpdateRegisteredUserResponseDto>> updateRegisteredUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRegisteredRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateRegisteredUser(userId, request, userDetails)));
    }

    /**
     * 관리자 정보 삭제
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 수정할 유저 정보
     * @param userDetails - jwt token
     *
     * @return NO_CONTENT
     */
    @DeleteMapping("/registered/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.deleteUser(userId, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }

    /**
     *  내 프로필 조회 (관리자 자신)
     * 접근 권한 : 모든 관리자.
     *
     * @param userDetails - jwt token
     *
     * @return 회원 상세 정보
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<GetUserResponseDto>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(ApiResponseDto.success(userService.getMyInfo(userDetails.getId())));
    }

    /**
     *  내 프로필 수정 (관리자 자신)
     * 접근 권한 : 모든 관리자.
     *
     * @param request - 수정 데이터  [이름, 이메일, 전화번호 수정 가능]
     * @param userDetails - jwt token
     *
     * @return 회원 상세 정보
     */
    @PatchMapping("/me/update")
    public ResponseEntity<ApiResponseDto<UpdateUserInfoResponseDto>> updateMyInfo(
            @Valid @RequestBody UpdateUserInfoRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateMyInfo(userDetails.getId(), request)));
    }

    /**
     *  내 프로필 수정 (관리자 자신)
     * 접근 권한 : 모든 관리자.
     *
     * @param request - 수정 데이터  [이름, 이메일, 전화번호 수정 가능]
     * @param userDetails - jwt token
     *
     * @return 회원 상세 정보
     */
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponseDto<UpdateMyPasswordResponseDto>> updateMyPassword(
            @Valid @RequestBody UpdateMyPasswordRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("세션이 존재하지 않습니다."));
        }
        userService.changeMyPassword(userDetails.getId(), request);
        return ResponseEntity.ok(ApiResponseDto.successWithNoContent());
    }

    /**
     *  플랫폼 이용하는 모든 고객 조회 (페이징)
     * 접근 권한 : 모든 관리자.
     *
     * @param page - 페이지
     * @param size - 페이지 당 데이터 크기
     * @param userDetails - jwt token
     *
     * @return 고객 정보 pagination
     */
    @GetMapping("/customers")
    public ResponseEntity<ApiResponseDto<Page<GetAllCustomerResponseDto>>> getAllCustomer(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getAllCustomer(page, size, userDetails)));
    }

    /**
     * 고객 정보 상세 조회
     * 접근 권한 : 모든 관리자.
     *
     * @param userId - 조회할 유저 ID
     * @param userDetails - jwt token
     *
     * @return 고객 정보
     */
    @GetMapping("/customers/{userId}")
    public ResponseEntity<ApiResponseDto<GetOneCustomerResponseDto>> getOneCustomer(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.getOneCustomer(userId, userDetails)));
    }

    /**
     * 고객 정보 수정
     * 접근 권한 : 모든 관리자.
     *
     * @param userId - 수정할 유저 ID
     * @param request - 슈정할 유저 정보
     * @param userDetails - jwt token
     *
     * @return 수정된 고객 정보
     */
    @PatchMapping("/customers/{userId}")
    public ResponseEntity<ApiResponseDto<UpdateCustomerInfoResponseDto>> updateCustomerInfo(
        @PathVariable Long userId,
        @RequestBody UpdateUserInfoRequestDto request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateCustomer(userId, request, userDetails)));
    }

    /**
     * 고객 상태 수정
     * 접근 권한 : 모든 관리자.
     *
     * @param userId - 수정할 유저 ID
     * @param request - 슈정할 유저 상태 정보
     * @param userDetails - jwt token
     *
     * @return 수정된 고객 정보
     */
    @PatchMapping("/customers/{userId}/status")
    public ResponseEntity<ApiResponseDto<UpdateCustomerInfoResponseDto>> updateCustomerStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateCustomerStatusRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.updateCustomerStatus(userId, request, userDetails)));
    }

    /**
     * 고객 회원 탈퇴
     * 접근 권한 : 모든 권한
     *
     * @param userId - 수정할 유저 ID
     * @param userDetails - jwt token
     *
     * @return NO_CONTENT
     */
    @DeleteMapping("/customers/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCustomer(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.deleteCustomer(userId, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}

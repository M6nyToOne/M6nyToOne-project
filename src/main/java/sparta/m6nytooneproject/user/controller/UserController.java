package sparta.m6nytooneproject.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ApiResponseDto<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        return ApiResponseDto.success(HttpStatus.CREATED,userService.createUser(requestDto));
    }

    /**
     * 회원 로그인
     *
     * @param request 로그인 아이디 비밀번호 정보
     * @return NO_CONTENT
     */
    @PostMapping("/login")
    public ApiResponseDto<Void> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        return ApiResponseDto.success(HttpStatus.CREATED ,null);
    }

    /**
     * 회원 로그아웃
     * 프론트에서 토큰을 파기하면 로그아웃이기 때문에 동작하는 로직은 없지만 session 방식 채용시 필요
     *
     * @return NO_CONTENT
     */
    @PostMapping("/logout")
    public ApiResponseDto<Void> logout() {
        return ApiResponseDto.successWithNoContent();
    }

    /**
     * 승인 대기중인 관리자 회원 목록 조회
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param page - 페이지
     * @param size - 페이지 당 데이터 크기
     *
     * @return 회원 정보 pagination
     */
    @GetMapping("/pendings")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<UserResponseDto> getPendingUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        return ApiResponseDto.pagination(HttpStatus.OK, userService.getPendingUsers(page, size),"성공적으로 조회하였습니다." );
    }

    /**
     * 승인 대기중인 관리자 승인으로 변경
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 변경할 유저 ID
     * @param request - 변경 상태, 거절시 거절사유 포함
     *
     * @return 변경된 유저 정보
     */
    @PatchMapping("/pendings/{userId}")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<UpdateUserStatusResponseDto> updatePendingUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequestDto request
    ) {
        return ApiResponseDto.success(HttpStatus.OK, userService.updatePendingUser(userId, request));
    }

    /**
     * 승인된 관리자만 조회
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param page - 페이지
     * @param size - 페이지 당 데이터 크기
     *
     * @return 승인된 관리자 정보 pagination
     */
    @GetMapping("/registered")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<UserResponseDto> getRegisteredUsers(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ApiResponseDto.pagination(HttpStatus.OK, userService.getRegisteredUsers(page, size),"성공적으로 조회하였습니다." );
    }

    /**
     * 승인된 관리자 상세 조회
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 조회할 유저 정보
     *
     * @return 상세 유저 정보
     */
    @GetMapping("/registered/{userId}")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<UserResponseDto> getOneRegisteredUser(
            @PathVariable Long userId
    ) {
        return ApiResponseDto.success(HttpStatus.OK, userService.getOneRegisteredUser(userId));
    }

    /**
     * 승인된 관리자 정보 수정
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 수정할 유저 정보
     * @param request - 수정할 정보 [이름, 이메일, 전화번호 수정 가능]
     *
     * @return 수정된 유저 정보
     */
    @PatchMapping("/registered/{userId}/info")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<UpdateUserInfoResponseDto> updateUserInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserInfoRequestDto request
    ) {
        return ApiResponseDto.success(HttpStatus.OK, userService.updateUserInfo(userId, request));
    }

    /**
     * 승인된 관리자 role 수정
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 수정할 유저 정보
     * @param request - 수정할 role
     *
     * @return 수정된 관리자 정보
     */
    @PatchMapping("/registered/{userId}/status")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<UpdateRegisteredUserResponseDto> updateRegisteredUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRegisteredRequestDto request
    ) {
        return ApiResponseDto.success(HttpStatus.OK,userService.updateRegisteredUser(userId, request));
    }

    /**
     * 관리자 정보 삭제
     * 접근 권한 : 슈퍼 관리자.
     *
     * @param userId - 수정할 유저 정보
     *
     * @return NO_CONTENT
     */
    @DeleteMapping("/registered/{userId}")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponseDto<Void> deleteUser(
            @PathVariable Long userId
    ) {
        userService.deleteUser(userId);
        return ApiResponseDto.successWithNoContent();
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
    @PreAuthorize("hasAnyRole('SUPER') or @userSecurity.isOwner(authentication, #userDetails.id)")
    public ApiResponseDto<GetUserResponseDto> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
       return ApiResponseDto.success(HttpStatus.OK,userService.getMyInfo(userDetails.getId()));
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
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') and @userSecurity.isOwner(authentication, #userDetails.id)")
    public ApiResponseDto<UpdateUserInfoResponseDto> updateMyInfo(
            @Valid @RequestBody UpdateUserInfoRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponseDto.success(HttpStatus.OK,userService.updateMyInfo(userDetails.getId(), request));
    }

    /**
     *  내 비밀번호 수정
     *
     * @param request - 수정 데이터
     * @param userDetails - jwt token
     *
     * @return 회원 상세 정보
     */
    @PatchMapping("/me/password")
    @PreAuthorize("@userSecurity.isOwner(authentication, #userDetails.id)")
    public ApiResponseDto<Void> updateMyPassword(
            @Valid @RequestBody UpdateMyPasswordRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.changeMyPassword(userDetails.getId(), request);
        return ApiResponseDto.successWithNoContent();
    }

    /**
     *  플랫폼 이용하는 모든 고객 조회 (페이징)
     * 접근 권한 : 모든 관리자.
     *
     * @param page - 페이지
     * @param size - 페이지 당 데이터 크기
     *
     * @return 고객 정보 pagination
     */
    @GetMapping("/customers")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ApiResponseDto<GetAllCustomerResponseDto> getAllCustomer(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponseDto.pagination(HttpStatus.OK, userService.getAllCustomer(page, size) ,"성공적으로 조회 하였습니다.");
    }

    /**
     * 고객 정보 상세 조회
     *
     * @param userId - 조회할 유저 ID
     *
     * @return 고객 정보
     */
    @GetMapping("/customers/{userId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @userSecurity.isOwner(authentication, #userId)")
    public ApiResponseDto<GetOneCustomerResponseDto> getOneCustomer(
            @PathVariable Long userId
    ) {
        return ApiResponseDto.success(HttpStatus.OK,userService.getOneCustomer(userId));
    }

    /**
     * 고객 정보 수정
     * 접근 권한 : 모든 관리자.
     *
     * @param userId - 수정할 유저 ID
     * @param request - 슈정할 유저 정보
     *
     * @return 수정된 고객 정보
     */
    @PatchMapping("/customers/{userId}")
    @PreAuthorize("hasAnyRole('SUPER') or @userSecurity.isOwner(authentication, #userId)")
    public ApiResponseDto<UpdateCustomerInfoResponseDto> updateCustomerInfo(
        @PathVariable Long userId,
        @RequestBody UpdateUserInfoRequestDto request
    ) {
        return ApiResponseDto.success(HttpStatus.OK ,userService.updateCustomer(userId, request));
    }

    /**
     * 고객 상태 수정
     * 접근 권한 : 모든 관리자.
     *
     * @param userId - 수정할 유저 ID
     * @param request - 슈정할 유저 상태 정보
     *
     * @return 수정된 고객 정보
     */
    @PatchMapping("/customers/{userId}/status")
    @PreAuthorize("hasAnyRole('SUPER') or @userSecurity.isOwner(authentication, #userId)")
    public ApiResponseDto<UpdateCustomerInfoResponseDto> updateCustomerStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateCustomerStatusRequestDto request
    ) {
        return ApiResponseDto.success(HttpStatus.OK, userService.updateCustomerStatus(userId, request));
    }

    /**
     * 고객 회원 탈퇴
     *
     * @param userId - 수정할 유저 ID
     *
     * @return NO_CONTENT
     */
    @DeleteMapping("/customers/{userId}")
    @PreAuthorize("hasAnyRole('SUPER') or @userSecurity.isOwner(authentication, #userId)")
    public ApiResponseDto<Void> deleteCustomer(
            @PathVariable Long userId
    ) {
        userService.deleteCustomer(userId);
        return ApiResponseDto.successWithNoContent();
    }
}

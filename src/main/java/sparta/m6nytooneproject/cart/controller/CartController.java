package sparta.m6nytooneproject.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.cart.dto.CartRequestDto;
import sparta.m6nytooneproject.cart.dto.CartResponseDto;
import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.cart.service.CartService;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.security.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/carts")//공통경로
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    //장바구니 생성
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponseDto<CartResponseDto> createCart(
            @Valid @RequestBody CartRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CartResponseDto result = cartService.createCart(request, userDetails);
        return ApiResponseDto.success(HttpStatus.CREATED, result);
    }

    //유저id로 전체조회
    @GetMapping
    public ApiResponseDto<List<CartResponseDto>> getAllCartsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Cart> cartPage = cartService.getCartPageByUserId(userId, page, size);
        return ApiResponseDto.success(HttpStatus.OK, cartService.getAllCartsByUserId(cartPage, userId));
    }

    //카트id로 단건조회
    @GetMapping("/{cartId}")
    public ApiResponseDto<CartResponseDto> getOneCart(
            @PathVariable Long cartId
    ) {
        return ApiResponseDto.success(HttpStatus.OK, cartService.getOneCart(cartId));
    }

    //카트id로 수정
    @PatchMapping("/{cartId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @cartSecurity.isSelf(authentication , #userId)")
    public ApiResponseDto<CartResponseDto> updateCart(
            @PathVariable Long cartId,
            @Valid @RequestBody CartRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CartResponseDto result = cartService.updateCart(cartId, request, userDetails.getId());
        return ApiResponseDto.success(HttpStatus.OK, result);
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @cartSecurity.isSelf(authentication , #userId)")
    public ApiResponseDto<CartResponseDto> deleteCart(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
       cartService.deleteCart(cartId, userDetails.getId());
       return ApiResponseDto.successWithNoContent();
    }
}

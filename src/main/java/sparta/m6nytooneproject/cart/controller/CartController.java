package sparta.m6nytooneproject.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.cart.dto.CartRequestDto;
import sparta.m6nytooneproject.cart.dto.CartResponseDto;
import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.cart.service.CartService;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/carts")//공통경로
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    //장바구니 생성
    @PostMapping
    public ResponseEntity<ApiResponseDto<CartResponseDto>> createCart(
            @Valid @RequestBody CartRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER, required = false) SessionUserDto loginUser
    ) {
        CartResponseDto result = cartService.createCart(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(result));
    }

    //유저id로 전체조회
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CartResponseDto>>> getAllCartsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Cart> cartPage = cartService.getCartPageByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponseDto.success(cartService.getAllCartsByUserId(cartPage, userId)));
    }

    //카트id로 단건조회
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> getOneCart(
            @PathVariable Long cartId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(cartService.getOneCart(cartId)));
    }

    //카트id로 수정
    @PatchMapping("/{cartId}")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> updateCart(
            @PathVariable Long cartId,
            @Valid @RequestBody CartRequestDto request,
            @SessionAttribute(name= AuthConstants.LOGIN_USER, required = false) SessionUserDto loginUser
    ) {
        CartResponseDto result = cartService.updateCart(cartId, request, loginUser.getId());
        return ResponseEntity.ok(ApiResponseDto.success(result));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> deleteCart(
            @PathVariable Long cartId,
            @SessionAttribute(name= AuthConstants.LOGIN_USER, required = false) SessionUserDto loginUser
    ) {
       cartService.deleteCart(cartId, loginUser.getId());
       return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}

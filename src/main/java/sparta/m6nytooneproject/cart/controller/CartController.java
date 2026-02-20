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
import sparta.m6nytooneproject.cart.repository.CartRepository;
import sparta.m6nytooneproject.cart.service.CartService;
import sparta.m6nytooneproject.global.dto.SessionUser;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/carts")//공통경로
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    //장바구니 생성
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@Valid @RequestBody CartRequestDto request) {
        CartResponseDto result = cartService.createCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //유저id로 전체조회
    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getAllCartsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Cart> cartPage = cartService.getCartPageByUserId(userId, page,size);

        return ResponseEntity.status(HttpStatus.OK).body(cartService.getAllCartByUserId(cartPage, userId));
    }

    //카트id로 단건조회
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getOneCart(@PathVariable Long cartId) {

        return ResponseEntity.status(HttpStatus.OK).body(cartService.getOneCart(cartId));
    }

    //카트id로 수정
    @PatchMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> updateCart(
            @PathVariable Long cartId,
            @RequestBody CartRequestDto request,
            @SessionAttribute(name= "login_user", required = false) SessionUser loginUser){

        CartResponseDto result = cartService.updateCart(cartId, request, loginUser.getId());

        return ResponseEntity.ok(result);

    }

}

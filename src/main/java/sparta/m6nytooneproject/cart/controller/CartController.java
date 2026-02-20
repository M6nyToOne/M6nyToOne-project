package sparta.m6nytooneproject.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.m6nytooneproject.cart.dto.CartRequestDto;
import sparta.m6nytooneproject.cart.dto.CartResponseDto;
import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.cart.repository.CartRepository;

@RestController
@RequestMapping("/carts")//공통경로
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartRepository cartRepository;

    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@Valid @RequestBody CartRequestDto request) {
        CartResponseDto response = cartService.addCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    /*
    전체조회시 s추가 : getAll(도메인명)

    단건조회시 : getOne(도메인명)

    수정 : update(도메인명)
    */
}

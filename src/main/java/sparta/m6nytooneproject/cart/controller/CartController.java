package sparta.m6nytooneproject.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.m6nytooneproject.cart.repository.CartRepository;

@RestController
@RequestMapping("/carts")//공통경로
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartRepository cartRepository;
}

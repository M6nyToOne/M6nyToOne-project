package sparta.m6nytooneproject.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.m6nytooneproject.cart.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
}

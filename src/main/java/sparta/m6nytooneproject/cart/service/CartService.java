package sparta.m6nytooneproject.cart.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import sparta.m6nytooneproject.cart.dto.CartRequestDto;
import sparta.m6nytooneproject.cart.dto.CartResponseDto;
import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.cart.repository.CartRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.user.dto.UserResponseDto;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository; // 고객 확인용
    private final ProductRepository productRepository; //상품 확인용


    public CartResponseDto createCart(@Valid @RequestBody CartRequestDto request) {

        // 1. 고객 존재 여부 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 고객입니다."));

        // 2. 존재하는 상품인지 확인
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        // 3. 판매 중인 상품인지 확인 (품절, 단종 체크)
        Status ProductStatus = product.getStatus();//존재하는 상품의 상태 확인
        if (!product.getStatus().equals(ProductStatus.ON_SALE)) {//상품상태 enum으로 생성되었는지 확인
            throw new IllegalStateException("현재 판매 중인 상품이 아닙니다. (상태: " + product.getStatus() + ")");
        }

        // 4. 기존 장바구니에 동일 상품이 있는지 확인
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        Cart cart;
        if (existingCart.isPresent()) {
            // 4-1. 동일 상품이 있으면 수량만 증가
            cart = existingCart.get();
            cart.updateQuantity(cart.getQuantity() + request.getQuantity());
        }
            // 5. 다른 상품인 경우 새로운 장바구니 생성
            cart = new Cart(request.getId(), request.getQuantity(), user, product);
        Cart savedCart = cartRepository.save(cart);



        return new CartResponseDto(savedCart);
    }
    }



}

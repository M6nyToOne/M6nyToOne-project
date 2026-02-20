package sparta.m6nytooneproject.cart.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import sparta.m6nytooneproject.cart.dto.CartRequestDto;
import sparta.m6nytooneproject.cart.dto.CartResponseDto;
import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.cart.repository.CartRepository;
import sparta.m6nytooneproject.global.dto.SessionUser;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.enums.Status;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository; // 고객 확인용
    private final ProductRepository productRepository; //상품 확인용

    //장바구니 생성
    public CartResponseDto createCart(@Valid @RequestBody CartRequestDto request, SessionUser loginUser) {

        // 1. 고객 존재 여부 확인
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 고객입니다."));

        //1-1. 고객 상태 체크 로직추가
        if(!user.getSignupStatus().equals(SignupStatus.ACTIVE)){
            throw new IllegalStateException("현재 유저가 활성 상태가 아닙니다.");
        }

        // 2. 존재하는 상품인지 확인
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        // 2-1. 판매 중인 상품인지 확인 (품절, 단종 체크)
        if (!product.getStatus().equals(Status.ON_SALE)) {//상품상태 enum으로 생성되었는지 확인
            throw new IllegalStateException("현재 판매 중인 상품이 아닙니다.");
        }

        // 3. 기존 장바구니에 동일 상품이 있는지 확인
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        Cart cart;
        if (existingCart.isPresent()) {
            // 3-1. 동일 상품이 있으면 수량만 증가
            cart = existingCart.get();
            cart.updateQuantity(cart.getQuantity() + request.getQuantity());
        }
            // 4. 다른 상품인 경우 새로운 장바구니 생성
            cart = new Cart(user.getId(), request.getQuantity(), user, product);
        Cart savedCart = cartRepository.save(cart);

        return new CartResponseDto(savedCart);

    }

    //페이징 처리 메서드
    public Page<Cart> getCartPageByUserId(Long userId,int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);
        return cartRepository.findAllByUserId(userId, pageable);
    }

    //장바구니 유저id로 전체조회(페이징)
    public List<CartResponseDto> getAllCartByUserId(
            Page<Cart> cartPage, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 고객입니다."));

        if(!user.getSignupStatus().equals(SignupStatus.ACTIVE)){
            throw new IllegalStateException("현재 유저가 활성 상태가 아닙니다.");
        }

        return cartPage.stream().map(CartResponseDto::new).toList();
    }

    //장바구니id로 단건조회
    public CartResponseDto getOneCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).
                orElseThrow(() -> new EntityNotFoundException("존재하지 않는 장바구니입니다."));

        return new CartResponseDto(cart);
    }

    //카트id로 수정(로그인한 본인이 만든 카트인지확인필요)
    public CartResponseDto updateCart(Long cartId, CartRequestDto request, Long loginUserId) {

        //해당id의 카트가 존재하는지 확인
        Cart cart = cartRepository.findById(cartId).
                orElseThrow(() -> new EntityNotFoundException("존재하지 않는 장바구니입니다."));
        //요청한 카트를 만든 유저id와 로그인한 유저id비교
        if (!cart.getUser().getId().equals(loginUserId)) {
            throw new IllegalStateException("해당 장바구니 수정 권한이 없습니다.");
        }
        cart.updateQuantity(request.getQuantity());

        return  new CartResponseDto(cart);
    }


    public void deleteCart(Long cartId, Long loginUserId) {

        Cart cart = cartRepository.findById(cartId).
                orElseThrow(() -> new EntityNotFoundException("존재하지 않는 장바구니입니다."));

        if (!cart.getUser().getId().equals(loginUserId)) {
            throw new IllegalStateException("해당 장바구니 수정 권한이 없습니다.");
        }
        cartRepository.delete(cart);
    }
}

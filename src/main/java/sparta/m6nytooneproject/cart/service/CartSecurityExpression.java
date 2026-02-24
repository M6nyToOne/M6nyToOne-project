package sparta.m6nytooneproject.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sparta.m6nytooneproject.cart.repository.CartRepository;
import sparta.m6nytooneproject.security.CustomUserDetails;

@Component("cartSecurity")
@RequiredArgsConstructor
public class CartSecurityExpression {

    private final CartRepository cartRepository;

    public boolean isSelf(Authentication authentication, Long cartId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return cartRepository.findById(cartId).map(cart -> cart.getUser().getId().equals(userDetails.getId()))
                .orElse(false);
    }
}

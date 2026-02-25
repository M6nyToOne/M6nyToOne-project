package sparta.m6nytooneproject.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.security.CustomUserDetails;

import java.util.UUID;

@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurityExpression {

    private final OrderRepository orderRepository;
    public boolean isOwner(Authentication authentication, UUID orderId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return orderRepository.findByOrderId(orderId)
                .map(order -> order.getCustomer().getId().equals(userDetails.getId()))
                .orElse(false);
    }

}

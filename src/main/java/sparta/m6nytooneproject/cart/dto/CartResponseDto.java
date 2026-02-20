package sparta.m6nytooneproject.cart.dto;

import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

public class CartResponseDto {

    final private Long id;
    final private int quantity;
    final private Long userId;
    final private Long productId;
    final private LocalDateTime createdAt;
    final private LocalDateTime updatedAt;

    public CartResponseDto(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.userId = cart.getUser().getId();
        this.productId = cart.getProduct().getId();
        this.createdAt = cart.getCreatedAt();
        this.updatedAt = cart.getModifiedAt();
    }
}

package sparta.m6nytooneproject.cart.dto;

import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

public class CartResponseDto {

    private Long id;
    private int quantity;
    private Long userId;
    private Long productId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public CartResponseDto(Long id, int quantity, Long userId, Long productId, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.quantity = quantity;
        this.userId = userId;
        this.productId = productId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}

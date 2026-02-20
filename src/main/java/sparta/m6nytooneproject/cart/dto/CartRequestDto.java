package sparta.m6nytooneproject.cart.dto;

import lombok.Getter;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.user.entity.User;


@Getter
public class CartRequestDto {

    private Long id;
    private int quantity;
    private Long userId;
    private Long productId;

    public CartRequestDto(Long id, int quantity, Long userId, Long productId) {
        this.id = id;
        this.quantity = quantity;
        this.userId = userId;
        this.productId = productId;
    }

    public Long getUserId(Long userId) {
        return userId = userId;
    }

    public Long getProductId(Long productId) {
        return productId =  productId;
    }
}

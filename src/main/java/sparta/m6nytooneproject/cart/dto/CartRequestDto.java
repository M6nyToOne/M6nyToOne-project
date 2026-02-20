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

}

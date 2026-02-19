package sparta.m6nytooneproject.order.dto;

import lombok.Getter;

@Getter
public class OrderRequest {
    private int quantity;
    private Long productId;
    private Long userId;
}

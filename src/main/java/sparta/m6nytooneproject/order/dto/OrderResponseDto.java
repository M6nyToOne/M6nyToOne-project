package sparta.m6nytooneproject.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {
    private final Long orderId;
    private final String productName;
    private final int productPrice;
    private final int totalPrice;
}

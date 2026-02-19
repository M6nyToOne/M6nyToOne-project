package sparta.m6nytooneproject.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.m6nytooneproject.order.entity.Order;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {
    private final Long orderId;
    private final String productName;
    private final int productPrice;
    private final int totalPrice;

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getProductName(),
                order.getProductPrice(),
                order.getProductPrice() * order.getQuantity()
        );
    }
}

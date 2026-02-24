package sparta.m6nytooneproject.dashboard.dto;

import lombok.Getter;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;

import java.util.UUID;

@Getter
public class GetRecentOrdersResponseDto {
    private final UUID orderId;
    private final String userName;
    private final String productName;
    private final int price;
    private final OrderStatus orderStatus;

    public GetRecentOrdersResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.userName = order.getCustomer().getUserName();
        this.productName = order.getProductName();
        this.price = order.getProductPrice();
        this.orderStatus = order.getStatus();
    }
}

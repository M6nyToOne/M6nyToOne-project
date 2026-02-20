package sparta.m6nytooneproject.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderListResponseDto {
    private final Long Id;
    private final String orderId;
    private final String customerName;
    private final String productName;
    private final int quantity;
    private final int price;
    private final String orderStatus;
    private final LocalDateTime createdAt;
    //cs 주문시 관리자 이름
    private final String adminName;

    public static OrderListResponseDto from(Order order) {
        User admin = order.getAdmin();

        String adminName = (admin != null) ? admin.getUserName() : null;

        return new OrderListResponseDto(
                order.getId(),
                order.getOrderId().toString(),
                order.getUserName(),
                order.getProductName(),
                order.getQuantity(),
                order.getProductPrice() * order.getQuantity(),
                order.getStatus().getStatus(),
                order.getCreatedAt(),
                adminName
        );
    }
}

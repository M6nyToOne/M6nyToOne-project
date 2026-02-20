package sparta.m6nytooneproject.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderDetailResponseDto {
    private final String orderId;
    private final String productName;
    private final String category;
    private final int price;
    private final int quantity;
    private final String orderStatus;
    private final String customerName;
    private final String customerEmail;
    private final LocalDateTime orderDate;

    //cs 주문시 관리자 정보
    private final String adminName;
    private final String adminEmail;
    private final String adminRole;

    public static OrderDetailResponseDto from(Order order) {
        // admin null 체크
        User admin = order.getAdmin();

        String adminName = (admin != null) ? admin.getUserName() : null;
        String adminEmail = (admin != null) ? admin.getEmail() : null;
        String adminRole = (admin != null && admin.getRole() != null)
                ? admin.getRole().getTitle()
                : null;

        return new OrderDetailResponseDto(
                order.getOrderId().toString(),
                order.getProductName(),
                order.getProduct().getCategory().getCategory(),
                order.getQuantity() * order.getProductPrice(),
                order.getQuantity(),
                order.getStatus().getStatus(),
                order.getUserName(),
                order.getCustomer().getEmail(),
                order.getCreatedAt(),
                adminName,
                adminEmail,
                adminRole
                );
    }
}
package sparta.m6nytooneproject.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import sparta.m6nytooneproject.order.entity.OrderStatus;

@Getter
public class updateOrderStatus {
    @NotBlank
    private OrderStatus status;
}

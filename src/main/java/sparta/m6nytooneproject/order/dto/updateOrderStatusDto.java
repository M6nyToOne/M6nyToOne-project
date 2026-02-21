package sparta.m6nytooneproject.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sparta.m6nytooneproject.order.entity.OrderStatus;

@Getter
public class updateOrderStatusDto {
    @NotNull
    private OrderStatus status;
}

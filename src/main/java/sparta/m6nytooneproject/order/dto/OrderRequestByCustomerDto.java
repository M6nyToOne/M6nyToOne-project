package sparta.m6nytooneproject.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class OrderRequestByCustomerDto {
    @Min(value = 1 , message = "수량은 1개 이상 이어야 합니다.")
    private Integer quantity;
    @Min(1)
    private Long productId;
}

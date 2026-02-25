package sparta.m6nytooneproject.order.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequestByCsDto {
    @Min(value = 0 , message = "수량은 0개 이상 이어야 합니다.")
    private Integer quantity;
    @Min(0)
    private Long productId;
}

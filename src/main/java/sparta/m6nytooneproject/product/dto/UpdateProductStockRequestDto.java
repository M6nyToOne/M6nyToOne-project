package sparta.m6nytooneproject.product.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateProductStockRequestDto {

    @Min(value = 0, message = "재고는 0개 이상이어야 합니다.")
    private int stock;
}

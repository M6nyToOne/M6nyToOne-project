package sparta.m6nytooneproject.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;

}

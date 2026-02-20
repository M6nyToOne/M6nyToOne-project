package sparta.m6nytooneproject.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartRequestDto {

    @NotNull(message = "장바구니 ID는 필수입니다.")
    private Long id;
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

}

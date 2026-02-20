package sparta.m6nytooneproject.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sparta.m6nytooneproject.product.enums.Category;

@Getter
public class UpdateProductRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 20, message = "상품명은 {max}자 이하이어야 합니다.")
    private String productName;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @Min(value = 0, message = "가격은 0원 이상어야 합니다.")
    private int price;
}

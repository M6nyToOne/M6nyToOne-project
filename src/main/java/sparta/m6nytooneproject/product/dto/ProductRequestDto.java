package sparta.m6nytooneproject.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.Status;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 20, message = "상품명은 {max}자 이하이어야 합니다.")
    private String productName;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    @Min(value = 0, message = "재고는 0개 이상이어야 합니다.")
    private int stock;

    @NotNull(message = "상품 상태 필수입니다.")
    private Status status;

}

package sparta.m6nytooneproject.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sparta.m6nytooneproject.product.enums.Category;
import sparta.m6nytooneproject.product.enums.Status;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 20, message = "상품명은 {max}자 이하이어야 합니다.")
    private String productName;

    @NotBlank(message = "카테고리는 필수입니다.")
    private Category category;

    @NotBlank(message = "가격은 필수입니다.")
    private int price;

    @NotBlank(message = "재고는 필수입니다.")
    private int stock;

    @NotBlank(message = "상품 상태 필수입니다.")
    private Status status;
}

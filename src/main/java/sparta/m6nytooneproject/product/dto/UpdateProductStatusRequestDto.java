package sparta.m6nytooneproject.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sparta.m6nytooneproject.product.entity.Status;

@Getter
public class UpdateProductStatusRequestDto {

    @NotNull(message = "재고는 필수입니다.")
    private Status status;
}

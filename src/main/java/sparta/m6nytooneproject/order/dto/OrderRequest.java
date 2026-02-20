package sparta.m6nytooneproject.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderRequest {
    @NotBlank
    private Integer quantity;
    @NotBlank
    private Long productId;
    @NotBlank
    private Long userId;

    private Long adminId;
}

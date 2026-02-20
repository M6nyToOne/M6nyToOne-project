package sparta.m6nytooneproject.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.m6nytooneproject.product.enums.Category;
import sparta.m6nytooneproject.product.enums.Status;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductResponseDto {

    private final Long id;
    private final String productName;
    private final Category category;
    private final int price;
    private final int stock;
    private final Status status;
    private final LocalDateTime createdAt;
    private final String createdBy;
}

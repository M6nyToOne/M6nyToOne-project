package sparta.m6nytooneproject.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.enums.Category;
import sparta.m6nytooneproject.product.enums.Status;

import java.time.LocalDateTime;

@Getter
public class GetOneProductResponseDto {

    private final Long id;
    private final String productName;
    private final Category category;
    private final int price;
    private final int stock;
    private final Status status;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final String createdByEmail;


    public GetOneProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.createdAt = product.getCreatedAt();
        this.createdBy = product.getUser().getUserName();
        this.createdByEmail = product.getUser().getEmail();
    }
}

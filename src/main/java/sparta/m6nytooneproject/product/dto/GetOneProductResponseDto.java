package sparta.m6nytooneproject.product.dto;

import lombok.Getter;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.enums.Category;
import sparta.m6nytooneproject.product.enums.Status;
import sparta.m6nytooneproject.review.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

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
    private final String averageRate;
    private final int reviewCount;
    private final String ratings;
    private final List<Review> recentReviews;

    public GetOneProductResponseDto(Product product, String averageRate, int reviewCount, String ratings, List<Review> recentReviews) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.createdAt = product.getCreatedAt();
        this.createdBy = product.getAdmin().getUserName();
        this.createdByEmail = product.getAdmin().getEmail();
        this.averageRate = averageRate;
        this.reviewCount = reviewCount;
        this.ratings = ratings;
        this.recentReviews = recentReviews;
    }
}

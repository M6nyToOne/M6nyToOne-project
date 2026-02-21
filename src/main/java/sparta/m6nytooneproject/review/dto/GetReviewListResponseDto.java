package sparta.m6nytooneproject.review.dto;

import lombok.Getter;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.review.entity.Review;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class GetReviewListResponseDto {
    private final Long id;
    private final int reviewRate;
    private final String content;
    private final User customer;
    private final Product product;
    private final Order order;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetReviewListResponseDto(Review review) {
        this.id = review.getId();
        this.reviewRate = review.getReviewRate();
        this.content = review.getContent();
        this.customer = review.getCustomer();
        this.product = review.getProduct();
        this.order = review.getOrder();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }
}

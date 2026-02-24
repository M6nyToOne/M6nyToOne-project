package sparta.m6nytooneproject.review.dto;

import lombok.Getter;
import sparta.m6nytooneproject.review.entity.Review;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class GetReviewListResponseDto {
    private final Long id;
    private final int reviewRate;
    private final String content;
    private final String customerName;
    private final String productName;
    private final UUID orderId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetReviewListResponseDto(Review review) {
        this.id = review.getId();
        this.reviewRate = review.getReviewRate();
        this.content = review.getContent();
        this.customerName = review.getCustomer().getUserName();
        this.productName = review.getProduct().getProductName();
        this.orderId = review.getOrder().getOrderId();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }
}

package sparta.m6nytooneproject.review.dto;

import lombok.Getter;
import sparta.m6nytooneproject.review.entity.Review;

import java.time.LocalDateTime;

@Getter
public class GetReviewDetailResponseDto {
    private final int reviewRate;
    private final String content;
    private final String productName;
    private final String userName;
    private final String userEmail;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetReviewDetailResponseDto(Review review) {
        this.reviewRate = review.getReviewRate();
        this.content = review.getContent();
        this.productName = review.getProduct().getProductName();
        this.userName = review.getCustomer().getUserName();
        this.userEmail = review.getCustomer().getEmail();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }
}

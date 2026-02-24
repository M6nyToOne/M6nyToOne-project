package sparta.m6nytooneproject.review.dto;

import lombok.Getter;
import sparta.m6nytooneproject.review.entity.Review;

import java.time.LocalDateTime;

@Getter
public class GetRecentReviewsDto {
    private final String userName;
    private final int reviewRate;
    private final String content;
    private final LocalDateTime createAt;

    public GetRecentReviewsDto(Review review) {
        this.userName = review.getCustomer().getUserName();
        this.reviewRate = review.getReviewRate();
        this.content = review.getContent();
        this.createAt = review.getCreatedAt();
    }
}

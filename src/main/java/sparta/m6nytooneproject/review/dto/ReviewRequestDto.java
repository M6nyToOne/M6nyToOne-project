package sparta.m6nytooneproject.review.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.User;

@Getter
public class ReviewRequestDto {
    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 1~5 사이의 정수로 입력해주세요.")
    @Max(value = 5, message = "평점은 1~5 사이의 정수로 입력해주세요.")
    private Integer reviewRate;
    private String content;
}

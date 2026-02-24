package sparta.m6nytooneproject.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.User;

@Getter
public class ReviewRequestDto {
    @Min(value = 1, message = "평점은 1~5 사이의 정수로 입력해주세요.")
    @Max(value = 5, message = "평점은 1~5 사이의 정수로 입력해주세요.")
    private int reviewRate;
    private String content;
}

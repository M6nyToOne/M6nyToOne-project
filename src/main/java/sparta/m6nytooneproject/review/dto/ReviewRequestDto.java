package sparta.m6nytooneproject.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.User;

@Getter
public class ReviewRequestDto {
    @NotBlank(message = "평점을 입력해주세요.")
    @Size(min = 1, max = 5, message = "평점은 1~5 사이의 정수로 입력해주세요.")
    private int reviewRate;
    private String content;
    @NotBlank
    private User customer;
}

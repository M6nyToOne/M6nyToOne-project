package sparta.m6nytooneproject.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.review.dto.GetReviewDetailResponseDto;
import sparta.m6nytooneproject.review.dto.GetReviewListResponseDto;
import sparta.m6nytooneproject.review.dto.ReviewRequestDto;
import sparta.m6nytooneproject.review.dto.ReviewResponseDto;
import sparta.m6nytooneproject.review.service.ReviewService;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponseDto<ReviewResponseDto>> createReview(
            @PathVariable Long orderId,
            @Valid @RequestBody ReviewRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(reviewService.createReview(orderId, request, sessionUser)));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponseDto<Page<GetReviewListResponseDto>>> getAllReviews(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) int reviewRate
    ) {
        return ResponseEntity
                .ok(ApiResponseDto.success(reviewService.getAllReviews(pageable, userName, productName, reviewRate)));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponseDto<GetReviewDetailResponseDto>> getOneReview(
            @PathVariable Long reviewId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(reviewService.getOneReview(reviewId)));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteReview(
            @PathVariable Long reviewId,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        reviewService.deleteReview(reviewId, sessionUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}

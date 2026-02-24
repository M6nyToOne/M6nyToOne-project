package sparta.m6nytooneproject.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.review.dto.GetReviewDetailResponseDto;
import sparta.m6nytooneproject.review.dto.GetReviewListResponseDto;
import sparta.m6nytooneproject.review.dto.ReviewRequestDto;
import sparta.m6nytooneproject.review.dto.ReviewResponseDto;
import sparta.m6nytooneproject.review.service.ReviewService;
import sparta.m6nytooneproject.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    @PreAuthorize("hasRole('CUSTOMER') and @orderSecurity.isOwner(authentication , #orderId)")
    public ApiResponseDto<ReviewResponseDto> createReview(
            @PathVariable Long orderId,
            @Valid @RequestBody ReviewRequestDto request
    ) {
       return ApiResponseDto.success(HttpStatus.OK , reviewService.createReview(orderId, request));
    }

    @GetMapping("/reviews")
    public ApiResponseDto<GetReviewListResponseDto> getAllReviews(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) int reviewRate
    ) {
        return ApiResponseDto.pagination(HttpStatus.OK,
                reviewService.getAllReviews(pageable, userName, productName, reviewRate) ,"성공적으로 조회하였습니다.");

    }

    @GetMapping("/reviews/{reviewId}")
    public ApiResponseDto<GetReviewDetailResponseDto> getOneReview(
            @PathVariable Long reviewId
    ) {
        return ApiResponseDto.success(HttpStatus.OK, reviewService.getOneReview(reviewId));
    }

    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ApiResponseDto<Void> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ApiResponseDto.successWithNoContent();
    }
}

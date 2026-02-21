package sparta.m6nytooneproject.review.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.global.exception.*;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.review.dto.GetReviewDetailResponseDto;
import sparta.m6nytooneproject.review.dto.GetReviewListResponseDto;
import sparta.m6nytooneproject.review.dto.ReviewRequestDto;
import sparta.m6nytooneproject.review.dto.ReviewResponseDto;
import sparta.m6nytooneproject.review.entity.Review;
import sparta.m6nytooneproject.review.repository.ReviewRepository;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponseDto createReview(Long orderId, @Valid ReviewRequestDto request, SessionUserDto sessionUser) {
        User user = userRepository.findById(sessionUser.getId()).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );
        if (!order.getCustomer().getId().equals(sessionUser.getId())) {
            throw new UnAuthurizedException("주문자 본인만 리뷰를 작성할 수 있습니다.");
        }
        if (reviewRepository.existsByOrderId(orderId)) {
            throw new AlreadyExistingReviewException("이미 리뷰를 작성한 주문입니다.");
        }
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new UnAuthurizedException("배달 완료된 주문만 리뷰를 작성할 수 있습니다.");
        }
        Review review = new Review(request.getReviewRate(), request.getContent(), user, order.getProduct(), order);
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    public Page<GetReviewListResponseDto> getAllReviews(Pageable pageable, String userName, String productName, int reviewRate) {
        Page<Review> reviews = reviewRepository.searchReview(pageable, userName, productName, reviewRate);
        return reviews.map(GetReviewListResponseDto::new);
    }

    public GetReviewDetailResponseDto getOneReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException("존재하지 않는 리뷰입니다.")
        );
        return new GetReviewDetailResponseDto(review);
    }

    public void deleteReview(Long reviewId, SessionUserDto sessionUser) {
        if (reviewRepository.existsById(reviewId)) {
            throw new ReviewNotFoundException("존재하지 않는 리뷰입니다.");
        }
        if (sessionUser.getUserRole().equals(UserRole.CUSTOMER)) {
            throw new UnAuthurizedException("관리자만 리뷰를 삭제할 수 있습니다.");
        }
        reviewRepository.deleteById(reviewId);
    }
}

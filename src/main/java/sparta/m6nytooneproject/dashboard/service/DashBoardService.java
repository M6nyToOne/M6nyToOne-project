package sparta.m6nytooneproject.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.dashboard.dto.GetSummaryResponseDto;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.review.repository.ReviewRepository;
import sparta.m6nytooneproject.security.CustomUserDetails;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;
import sparta.m6nytooneproject.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashBoardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    // Summary 통계
    public GetSummaryResponseDto getSummary(CustomUserDetails userDetails) {
        userService.isAdmin(userDetails);
        // 활성 관리자 수
        long activeAdminCount = userRepository.countByRoleNotAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE);
        // 활성 고객 수
        long activeCustomerCount = userRepository.countByRoleAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE);
        // 전체 상품 수 (재고 5개 이하인 상품)
        long productCount = productRepository.countByStockLessThanEqual(5);
        // 전체 주문 수 (오늘 주문 수)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long orderCount = orderRepository.countByCreatedAtAfter(startOfDay);
        // 전체 리뷰 수 ( 평균 평점)
        Double reviewCount = reviewRepository.findAverageRating();
        double finalReview = (reviewCount != null) ? reviewCount : 0.0;
        return new GetSummaryResponseDto(
                activeAdminCount,
                activeCustomerCount,
                productCount,
                orderCount,
                finalReview
        );
    }


    // Widgets 데이터

    // Charts 데이터

    // 최근 주문 목록
}

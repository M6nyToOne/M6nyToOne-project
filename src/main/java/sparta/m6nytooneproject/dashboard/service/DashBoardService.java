package sparta.m6nytooneproject.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.dashboard.dto.GetChartResponseDto;
import sparta.m6nytooneproject.dashboard.dto.GetRecentOrdersResponseDto;
import sparta.m6nytooneproject.dashboard.dto.GetSummaryResponseDto;
import sparta.m6nytooneproject.dashboard.dto.GetWidgetResponseDto;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.Status;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.review.repository.ReviewRepository;
import sparta.m6nytooneproject.security.CustomUserDetails;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.repository.UserRepository;
import sparta.m6nytooneproject.user.service.UserService;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public GetSummaryResponseDto getSummary() {
        // 활성 관리자 수
        long activeAdminCount = userRepository.countByRoleNotAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE);
        // 활성 고객 수
        long activeCustomerCount = userRepository.countByRoleAndSignupStatus(UserRole.CUSTOMER, SignupStatus.ACTIVE);
        // 전체 상품 수 (재고 5개 이하인 상품)
        long stockLessThan5ProductCount = productRepository.countByStockLessThanEqual(5);
        // 전체 주문 수 (오늘 주문 수)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long orderCount = orderRepository.countByCreatedAtAfter(startOfDay);
        // 전체 리뷰 수 ( 평균 평점)
        Double reviewCount = reviewRepository.findAverageRating();
        double finalReview = (reviewCount != null) ? reviewCount : 0.0;
        return new GetSummaryResponseDto(
                activeAdminCount,
                activeCustomerCount,
                stockLessThan5ProductCount,
                orderCount,
                finalReview
        );
    }

    // Widgets 데이터
    public GetWidgetResponseDto getWidget() {
        // 총 매출 (오늘 매출)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        List<Order> todayOrders = orderRepository.findAllByCreatedAtAfter(startOfDay);
        List<Integer> todayOrdersPrices = todayOrders.stream().map(Order::getProductPrice).toList();
        long todaySales = todayOrdersPrices.stream().mapToInt(Integer::intValue).sum();
        // 준비 중 주문 수
        long preparedOrderCount = orderRepository.countByStatus(OrderStatus.PREPARED);
        // 배송 중 주문 수
        long deliveredOrderCount = orderRepository.countByStatus(OrderStatus.DELIVERED);
        // 배송 완료 주문 수
        long completedOrderCount = orderRepository.countByStatus(OrderStatus.COMPLETED);
        // 전체 상품 수 (재고 5개 이하인 상품)
        long stockLessThan5ProductCount = productRepository.countByStockLessThanEqual(5);
        // 품절 상품 수
        long soldoutProducts = productRepository.countByStatus(Status.SOLD_OUT);
        return new GetWidgetResponseDto(
                todaySales,
                preparedOrderCount,
                deliveredOrderCount,
                completedOrderCount,
                stockLessThan5ProductCount,
                soldoutProducts
        );
    }

    // Charts 데이터
    public GetChartResponseDto getChart() {
        // 별점별 개수
        Map<Integer, Integer> rating = new HashMap<>();
        rating.put(5, reviewRepository.findRateReview(5).size());
        rating.put(4, reviewRepository.findRateReview(4).size());
        rating.put(3, reviewRepository.findRateReview(3).size());
        rating.put(2, reviewRepository.findRateReview(2).size());
        rating.put(1, reviewRepository.findRateReview(1).size());
        ObjectMapper mapper = new ObjectMapper();
        String ratingCounts = mapper.writeValueAsString(rating);
        // 상태별 고객 수
        Map<SignupStatus, Integer> statusMap = new HashMap<>();
        statusMap.put(SignupStatus.ACTIVE, userRepository.countBySignupStatus(SignupStatus.ACTIVE));
        statusMap.put(SignupStatus.INACTIVE, userRepository.countBySignupStatus(SignupStatus.INACTIVE));
        statusMap.put(SignupStatus.PENDING, userRepository.countBySignupStatus(SignupStatus.PENDING));
        statusMap.put(SignupStatus.SUSPEND, userRepository.countBySignupStatus(SignupStatus.SUSPEND));
        statusMap.put(SignupStatus.REJECTED, userRepository.countBySignupStatus(SignupStatus.REJECTED));
        ObjectMapper mapper1 = new ObjectMapper();
        String userCountByStatus = mapper1.writeValueAsString(statusMap);
        // 카테고리별 상품 수
        Map<Category, Integer> categoryMap = new HashMap<>();
        categoryMap.put(Category.ELECTRONICS, productRepository.countByCategory(Category.ELECTRONICS));
        categoryMap.put(Category.FASHION, productRepository.countByCategory(Category.FASHION));
        categoryMap.put(Category.FOOD, productRepository.countByCategory(Category.FOOD));
        ObjectMapper mapper2 = new ObjectMapper();
        String productCountByCategory = mapper2.writeValueAsString(categoryMap);
        return new GetChartResponseDto(ratingCounts, userCountByStatus, productCountByCategory);
    }

    // 최근 주문 목록
    public List<GetRecentOrdersResponseDto> getRecentOrder() {
        List<Order> orders = orderRepository.findTop10ByOrderByCreatedAtDesc();
        return orders.stream().map(GetRecentOrdersResponseDto::new).toList();
    }
}

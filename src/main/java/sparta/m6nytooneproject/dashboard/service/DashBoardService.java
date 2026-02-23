package sparta.m6nytooneproject.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.dashboard.dto.GetSummaryResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.review.repository.ReviewRepository;
import sparta.m6nytooneproject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashBoardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    // Summary 통계
    public GetSummaryResponseDto getSummary(SessionUserDto sessionUser) {
        // TODO: 대시보드를 구현해보자...
        return null;
    }

    // Widgets 데이터

    // Charts 데이터

    // 최근 주문 목록
}

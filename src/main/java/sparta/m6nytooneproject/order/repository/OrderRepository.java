package sparta.m6nytooneproject.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.m6nytooneproject.order.dto.CustomerOrderSummaryDto;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT o
        FROM Order o
        WHERE (:userName IS NULL OR :userName = '' OR o.userName = :userName)
          AND (:orderStatus IS NULL OR o.status = :orderStatus)
    """)
    Page<Order> searchByName(
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("userName") String userName,
                             Pageable pageable);

    @Query("""
    SELECT o
    FROM Order o
    WHERE (:customerId IS NOT NULL AND o.customer.id = :customerId)
    """)
    Page<Order> searchByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    // 고객 리스트 조회 데이터 확장 (총 주문 수, 총 구매 금액)
    @Query("""
        SELECT new sparta.m6nytooneproject.order.dto.CustomerOrderSummaryDto(o.customer.id, count(o.id), coalesce(sum(o.productPrice), 0L) )
        FROM Order o
        WHERE o.customer.id IN :customerIds
        AND o.status = sparta.m6nytooneproject.order.entity.OrderStatus.COMPLETED
        GROUP BY o.customer.id
    """)
    List<CustomerOrderSummaryDto> summaryCustomerOrder(@Param("customerIds")List<Long> customerIds);

    // 고객 상세 조회 데이터 확장 (총 주문 수, 총 구매 금액)
    @Query("""
        SELECT new sparta.m6nytooneproject.order.dto.CustomerOrderSummaryDto(o.customer.id, count(o.id), coalesce(sum(o.productPrice), 0L) )
        FROM Order o
        WHERE o.customer.id IN :customerId
        AND o.status = sparta.m6nytooneproject.order.entity.OrderStatus.COMPLETED
        GROUP BY o.customer.id
    """)
    Optional<CustomerOrderSummaryDto> summaryOneCustomerOrder(@Param("customerId")Long customerId);

    // 전체 주문 수 (오늘 주문 수) 를 위한 메서드
    long countByCreatedAtAfter(LocalDateTime localDateTime);

    List<Order> findAllByCreatedAtAfter(LocalDateTime startOfDay);

    long countByStatus(OrderStatus orderStatus);

    List<Order> findTop10ByOrderByCreatedAtDesc();

    Optional<Order> findByOrderId(UUID orderId);
}
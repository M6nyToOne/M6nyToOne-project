package sparta.m6nytooneproject.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.m6nytooneproject.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT o
        FROM Order o
        WHERE (:userName IS NULL OR :userName = '' OR o.userName = :userName)
          AND (:orderId IS NULL OR o.id = :orderId)
    """)
    Page<Order> search(@Param("userName") String userName,
                       @Param("orderId") Long orderId,
                       Pageable pageable);
}

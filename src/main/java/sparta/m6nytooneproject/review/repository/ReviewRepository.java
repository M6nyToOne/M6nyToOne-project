package sparta.m6nytooneproject.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.m6nytooneproject.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderId(Long orderId);

    @Query("""
            SELECT r
            FROM Review r
            WHERE (:userName IS NULL OR :userName = '' OR r.customer.userName = :username)
            AND (:productName IS NULL OR :productName = '' OR r.product.productName = :productName)
            AND (:reviewRate IS NULL OR r.reviewRate = :reviewRate)
            """)
    Page<Review> searchReview(Pageable pageable, @Param("userName") String userName, @Param("productName") String productName, @Param("reviewRate") int reviewRate);

    List<Review> findAllReviewByProductId(Long productId);

    @Query("SELECT r FROM Review r WHERE :rate IS NULL OR r.reviewRate = :rate")
    List<Review> findRateReviewByProductId(Long productId, @Param("rate") int rate);

    List<Review> findTop3ByProductIdOrderByCreatedAtDesc(Long productId);
}

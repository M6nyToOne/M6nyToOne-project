package sparta.m6nytooneproject.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.Status;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductNameContaining(String productName, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByStatus(Status status, Pageable pageable);

    Page<Product> findByProductNameContainingAndCategory(String productName, Category category, Pageable pageable);

    Page<Product> findByProductNameContainingAndStatus(String productName, Status status, Pageable pageable);

    Page<Product> findByCategoryAndStatus(Category category, Status status, Pageable pageable);

    Page<Product> findByProductNameContainingAndCategoryAndStatus(String productName, Category category, Status status, Pageable pageable);

    // 재고가 5개 이하인 전체 상품 수
    long countByStockLessThanEqual(int stock);

    long countByStatus(Status status);

    int countByCategory(Category category);
}


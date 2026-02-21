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

    Page<Product> findByProductNameAndCategory(String productName, Category category, Pageable pageable);

    Page<Product> findByProductNameAndStatus(String productName, Status status, Pageable pageable);

    Page<Product> findByCategoryAndStatus(Category category, Status status, Pageable pageable);

    Page<Product> findByProductNameAndCategoryAndStatus(String productName, Category category, Status status, Pageable pageable);
}


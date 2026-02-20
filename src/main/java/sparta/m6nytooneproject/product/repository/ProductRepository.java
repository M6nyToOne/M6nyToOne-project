package sparta.m6nytooneproject.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SearchResults;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.enums.Category;
import sparta.m6nytooneproject.product.enums.Status;
import sparta.m6nytooneproject.user.entity.User;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductNameContaining(Pageable pageable, String productName);
}


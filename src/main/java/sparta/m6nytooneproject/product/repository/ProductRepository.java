package sparta.m6nytooneproject.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

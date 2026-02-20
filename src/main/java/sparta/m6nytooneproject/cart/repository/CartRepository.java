package sparta.m6nytooneproject.cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.cart.entity.Cart;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserAndProduct(User user, Product product);

    Page<Cart> findAllByUserId(Long userId, Pageable pageable);
}

package sparta.m6nytooneproject.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.m6nytooneproject.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {


}

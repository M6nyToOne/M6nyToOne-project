package sparta.m6nytooneproject.cart.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import sparta.m6nytooneproject.global.entity.BaseEntity;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.user.entity.User;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "deleted")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    // 유저매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //상품 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    //생성자
    public Cart(Long id, int quantity, User user, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.user = user;
        this.product = product;
    }

    public void updateQuantity(int newQuantity) {
        // 필요 시 여기서 재고 수량과 비교하는 로직을 추가할 수 있습니다.
        this.quantity = newQuantity;
    }
}

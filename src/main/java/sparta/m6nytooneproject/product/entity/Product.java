package sparta.m6nytooneproject.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import sparta.m6nytooneproject.global.entity.BaseEntity;
import sparta.m6nytooneproject.user.entity.User;


@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "deleted")
public class Product extends BaseEntity {

    private final int SOLD_OUT_VALUE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    public Product(String productName, Category category, int price, int stock, Status status, User admin) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.admin = admin;
    }

    public void updateProduct(String productName, Category category, int price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    public void updateProductStock(int stock) {
        this.stock = stock;
    }

    public void discontinuedProduct() {
        this.stock = SOLD_OUT_VALUE;
        this.status = Status.DISCONTINUED;
    }

    public void soldOutProduct() {
        this.stock = SOLD_OUT_VALUE;
        this.status = Status.SOLD_OUT;
    }

    public void onSaleProduct() {
        this.status = Status.ON_SALE;
    }


    public void updateProductStatus(Status status) {
        this.status = status;
    }
}

package sparta.m6nytooneproject.order.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.m6nytooneproject.global.entity.BaseEntity;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.user.entity.User;

import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36) // uuid 의 길이 36
    private UUID orderId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    @Column
    private String cancelReason;

    @Column(nullable = false)
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    public void cancelOrder(String reason) {
        this.status = OrderStatus.CANCELLED;
        this.cancelReason = reason;
    }

    public void completeOrder() {
        this.status = OrderStatus.COMPLETED;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public Order(int productPrice, int quantity, OrderStatus status, String productName , String userName, Product product , User customer , User admin) {
        this.productPrice = productPrice;
        this.orderId = UUID.randomUUID();
        this.quantity = quantity;
        this.status = status;
        this.productName = productName;
        this.userName = userName;
        this.product = product;
        this.customer = customer;
        this.admin = admin;
    }
}

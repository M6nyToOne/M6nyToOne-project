package sparta.m6nytooneproject.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PREPARED("준비중"),
    CANCELLED("취소됨"),
    DELIVERED("배송중"),
    COMPLETED("배송 완료");

    private final String status;

    private OrderStatus(String status) {
        this.status = status;
    }
}

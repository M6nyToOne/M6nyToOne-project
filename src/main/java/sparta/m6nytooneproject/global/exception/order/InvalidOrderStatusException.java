package sparta.m6nytooneproject.global.exception.order;

import sparta.m6nytooneproject.order.entity.OrderStatus;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(OrderStatus status) {
        super("현재 주문 상태(" + status + ")에서는 취소할 수 없습니다.");
    }
}
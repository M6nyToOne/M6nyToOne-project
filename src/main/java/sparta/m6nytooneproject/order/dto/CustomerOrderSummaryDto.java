package sparta.m6nytooneproject.order.dto;

import lombok.Getter;

@Getter
public class CustomerOrderSummaryDto {

    // Lv.1 주문 기반 집계 정보를 위한 dto (총 주문 수, 총 구매 금액)

    private final Long userId;
    private final Long orderCount;
    private final Long totalAmount;

    public CustomerOrderSummaryDto(Long userId, Long orderCount, Long totalAmount) {
        this.userId = userId;
        this.orderCount = orderCount;
        this.totalAmount = totalAmount;
    }
}

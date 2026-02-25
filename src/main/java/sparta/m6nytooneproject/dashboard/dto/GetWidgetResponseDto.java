package sparta.m6nytooneproject.dashboard.dto;

import lombok.Getter;

@Getter
public class GetWidgetResponseDto {
    private final Long todaySales;
    private final Long preparedOrders;
    private final Long deliveredOrders;
    private final Long completedOrders;
    private final Long stockLessThan5ProductCount;
    private final Long soldoutProductCount;

    public GetWidgetResponseDto(Long todaySales, Long preparedOrders, Long deliveredOrders, Long completedOrders, Long stockLessThan5ProductCount, Long soldoutProductCount) {
        this.todaySales = todaySales;
        this.preparedOrders = preparedOrders;
        this.deliveredOrders = deliveredOrders;
        this.completedOrders = completedOrders;
        this.stockLessThan5ProductCount = stockLessThan5ProductCount;
        this.soldoutProductCount = soldoutProductCount;
    }
}

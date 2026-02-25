package sparta.m6nytooneproject.dashboard.dto;

import lombok.Getter;

@Getter
public class GetSummaryResponseDto {

    private final Long adminCount;
    private final Long customerCount;
    private final Long stockLessThan5ProductCount;
    private final Long orderCount;
    private final Double reviewCount;

    public GetSummaryResponseDto(Long adminCount, Long customerCount, Long stockLessThan5ProductCount, Long orderCount, Double reviewCount) {
        this.adminCount = adminCount;
        this.customerCount = customerCount;
        this.stockLessThan5ProductCount = stockLessThan5ProductCount;
        this.orderCount = orderCount;
        this.reviewCount = reviewCount;
    }
}

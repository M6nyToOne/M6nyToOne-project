package sparta.m6nytooneproject.dashboard.dto;

import lombok.Getter;

@Getter
public class GetSummaryResponseDto {

    private final Long adminCount;
    private final Long customerCount;
    private final Long productCount;
    private final Long orderCount;
    private final Long reviewCount;

    public GetSummaryResponseDto(Long adminCount, Long customerCount, Long productCount, Long orderCount, Long reviewCount) {
        this.adminCount = adminCount;
        this.customerCount = customerCount;
        this.productCount = productCount;
        this.orderCount = orderCount;
        this.reviewCount = reviewCount;
    }
}

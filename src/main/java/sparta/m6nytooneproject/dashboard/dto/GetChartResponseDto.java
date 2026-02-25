package sparta.m6nytooneproject.dashboard.dto;

import lombok.Getter;

@Getter
public class GetChartResponseDto {
    private final String ratingCounts;
    private final String userCountByStatus;
    private final String productCountByCategory;

    public GetChartResponseDto(String ratingCounts, String userCountByStatus, String productCountByCategory) {
        this.ratingCounts = ratingCounts;
        this.userCountByStatus = userCountByStatus;
        this.productCountByCategory = productCountByCategory;
    }
}

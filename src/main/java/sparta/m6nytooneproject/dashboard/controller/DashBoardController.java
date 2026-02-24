package sparta.m6nytooneproject.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.m6nytooneproject.dashboard.dto.GetChartResponseDto;
import sparta.m6nytooneproject.dashboard.dto.GetRecentOrdersResponseDto;
import sparta.m6nytooneproject.dashboard.dto.GetSummaryResponseDto;
import sparta.m6nytooneproject.dashboard.dto.GetWidgetResponseDto;
import sparta.m6nytooneproject.dashboard.service.DashBoardService;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
@PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
public class DashBoardController {

    private final DashBoardService dashBoardService;

    // Summary 통계
    @GetMapping("/summary")
    public ApiResponseDto<GetSummaryResponseDto> getSummary() {
        return ApiResponseDto.success(HttpStatus.OK, dashBoardService.getSummary());
    }

    // Widgets 데이터
    @GetMapping("/widgets")
    public ApiResponseDto<GetWidgetResponseDto> getWidget() {
        return ApiResponseDto.success(HttpStatus.OK,dashBoardService.getWidget());
    }

    // Charts 데이터
    @GetMapping("/charts")
    public ApiResponseDto<GetChartResponseDto> getChart() {
        return ApiResponseDto.success(HttpStatus.OK, dashBoardService.getChart());
    }

    // 최근 주문 목록
    @GetMapping("/recentOrders")
    public ApiResponseDto<List<GetRecentOrdersResponseDto>> getRecentOrder() {
        return ApiResponseDto.success(HttpStatus.OK, dashBoardService.getRecentOrder());
    }
}

package sparta.m6nytooneproject.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.m6nytooneproject.dashboard.dto.GetSummaryResponseDto;
import sparta.m6nytooneproject.dashboard.service.DashBoardService;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class DashBoardController {

    private final DashBoardService dashBoardService;

    // Summary 통계
    @GetMapping("/summary")
    public ApiResponseDto<GetSummaryResponseDto> getSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponseDto.success(HttpStatus.OK,dashBoardService.getSummary(userDetails));
    }

    // Widgets 데이터

    // Charts 데이터

    // 최근 주문 목록
}

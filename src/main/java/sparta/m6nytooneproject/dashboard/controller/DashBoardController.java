package sparta.m6nytooneproject.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import sparta.m6nytooneproject.dashboard.dto.GetSummaryResponseDto;
import sparta.m6nytooneproject.dashboard.service.DashBoardService;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class DashBoardController {

    private final DashBoardService dashBoardService;

    // Summary 통계
    @GetMapping("/summary")
    public ResponseEntity<ApiResponseDto<GetSummaryResponseDto>> getSummary(
            @SessionAttribute(name = AuthConstants.LOGIN_USER)SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(dashBoardService.getSummary(sessionUser)));
    }

    // Widgets 데이터

    // Charts 데이터

    // 최근 주문 목록
}

package sparta.m6nytooneproject.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.order.dto.*;
import sparta.m6nytooneproject.order.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/customers")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> createOrder(
            @RequestBody @Valid OrderRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(orderService.createOrderByCustomer(request ,sessionUser.getId())));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<OrderListResponseDto>>> getAllOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long orderId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.getAllOrders(pageable ,username ,orderId)));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> getOneOrder(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.getOneOrder(orderId)));
    }

    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> completeOrder(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.completeOrder(orderId)));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid updateOrderStatusDto status
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.updateOrderStatus(orderId , status.getStatus())));
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String cancelReason,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        orderService.cancelOrder(sessionUser.getId(), orderId, cancelReason);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}

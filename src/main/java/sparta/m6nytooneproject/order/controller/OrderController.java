package sparta.m6nytooneproject.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.order.dto.*;
import sparta.m6nytooneproject.order.entity.OrderSort;
import sparta.m6nytooneproject.order.service.OrderService;
import sparta.m6nytooneproject.security.CustomUserDetails;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{cartId}/customers")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> createOrderByCustomer(
            @RequestBody @Valid OrderRequestByCustomerDto request,
            @PathVariable Long cartId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(orderService.createOrderByCustomer(request, userDetails.getId(), cartId)));
    }
    @PostMapping("/{customerId}/cs")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> createOrderByCs(
            @RequestBody @Valid OrderRequestByCsDto request,
            @PathVariable Long customerId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(orderService.createOrderByAdmin(request, customerId, userDetails.getId())));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<OrderListResponseDto>>> getAllOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false , defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "CREATED_AT")  OrderSort orderSort
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.getAllOrders(page, size,orderSort, username)));
    }

    @GetMapping("{orderId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @orderSecurity.isOwner(authentication , #orderId)")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> getOneOrder(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.getOneOrder(orderId)));
    }

    @PatchMapping("/{orderId}/complete")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> completeOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.completeOrder(orderId)));
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(orderService.updateOrderStatus(orderId,userDetails.getRole())));
    }

    @DeleteMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @orderSecurity.isOwner(authentication , #orderId)")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String cancelReason,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        orderService.cancelOrder(userDetails, orderId, cancelReason);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}

package sparta.m6nytooneproject.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.order.dto.*;
import sparta.m6nytooneproject.order.entity.OrderSort;
import sparta.m6nytooneproject.order.entity.OrderStatus;
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
    public ApiResponseDto<OrderDetailResponseDto> createOrderByCustomer(
            @RequestBody @Valid OrderRequestByCustomerDto request,
            @PathVariable Long cartId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ApiResponseDto.success(HttpStatus.CREATED,
                orderService.createOrderByCustomer(request, userDetails.getId(), cartId)
                );
    }

    @PostMapping("/{customerId}/cs")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ApiResponseDto<OrderDetailResponseDto> createOrderByCs(
            @RequestBody @Valid OrderRequestByCsDto request,
            @PathVariable Long customerId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ApiResponseDto.success(HttpStatus.OK,
                orderService.createOrderByAdmin(request, customerId, userDetails.getId())
        );
    }

    @GetMapping("/lists")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ApiResponseDto<OrderListResponseDto> getAllOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false , defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false, defaultValue = "CREATED_AT")  OrderSort orderSort
    ) {
        return ApiResponseDto.pagination(HttpStatus.OK,
                orderService.getAllOrders(page, size, orderSort, username, status),
                "정상적으로 조회 되었습니다"
        );
    }

    @GetMapping("/list/customers")
    public ApiResponseDto<OrderListResponseDto> getAllOrdersByCustomer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false , defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "CREATED_AT")  OrderSort orderSort
    ) {
        return ApiResponseDto.pagination(HttpStatus.OK,
                orderService.getOrdersByCustomerId(page, size,orderSort, userDetails.getId()),
                "정상적으로 조회 되었습니다"
        );
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @orderSecurity.isOwner(authentication , #orderId)")
    public ApiResponseDto<OrderDetailResponseDto> getOneOrder(
            @PathVariable Long orderId
    ) {
        return ApiResponseDto.success(HttpStatus.OK,
                orderService.getOneOrder(orderId)
        );
    }

    @PatchMapping("/{orderId}/complete")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ApiResponseDto<OrderDetailResponseDto> completeOrder(
            @PathVariable Long orderId
    ) {
        return ApiResponseDto.success(HttpStatus.OK,
                orderService.completeOrder(orderId)
        );
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS')")
    public ApiResponseDto<OrderDetailResponseDto> updateOrderStatus(
            @PathVariable Long orderId
    ) {
        return ApiResponseDto.success(HttpStatus.OK,orderService.updateOrderStatus(orderId));
    }

    @DeleteMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('SUPER','OPER','MARKET','CS') or @orderSecurity.isOwner(authentication , #orderId)")
    public ApiResponseDto<Void> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String cancelReason
    ) {
        orderService.cancelOrder(orderId, cancelReason);
        return ApiResponseDto.successWithNoContent();
    }
}

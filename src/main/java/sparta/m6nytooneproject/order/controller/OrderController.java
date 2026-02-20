package sparta.m6nytooneproject.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.order.dto.*;
import sparta.m6nytooneproject.order.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDetailResponseDto> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest));
    }

    @GetMapping
    public ResponseEntity<Page<OrderListResponseDto>> getAllOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long orderId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getAllOrders(pageable ,username ,orderId));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOneOrder(@PathVariable Long orderId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrderDetail(orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String cancelReason
    ) {
        orderService.cancelOrder(orderId, cancelReason);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<OrderDetailResponseDto> completeOrder(
            @PathVariable Long orderId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.completeOrderStatus(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDetailResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid updateOrderStatus status
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.updateOrderStatus(orderId , status.getStatus()));
    }
}

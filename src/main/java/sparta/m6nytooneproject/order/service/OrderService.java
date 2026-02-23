package sparta.m6nytooneproject.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.global.exception.order.CancelOrderException;
import sparta.m6nytooneproject.global.exception.order.OrderException;
import sparta.m6nytooneproject.global.exception.order.OrderNotFoundException;
import sparta.m6nytooneproject.order.dto.OrderDetailResponseDto;
import sparta.m6nytooneproject.order.dto.OrderListResponseDto;
import sparta.m6nytooneproject.order.dto.OrderRequestDto;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.service.ProductService;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;


    @Transactional
    public OrderDetailResponseDto createOrderByCustomer(OrderRequestDto request, Long customerId) {
        User customer = userService.getUserById(customerId);

        // 고객이 주문하는게 맞는지 검증.
        userService.checkValidCustomer(customer.getRole());

        Product validProduct = productService.checkProductStock(request.getProductId() , request.getQuantity());

        // admin 없는 주문 생성.
        Order order = new Order(
                validProduct.getPrice(),
                request.getQuantity(),
                OrderStatus.PREPARED,
                validProduct.getProductName(),
                customer.getUserName(),
                validProduct,
                customer
        );
        orderRepository.save(order);
        //product 수량 조절 서비스 호출
        productService.decreaseStock(validProduct, request.getQuantity());
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public void cancelOrder(Long requestUserId, Long orderId, String cancelReason) {
        Order order = getOrderById(orderId);
        userService.validateRequesterIsOwner(requestUserId, order.getCustomer().getId());
        order.cancelOrder(cancelReason);

        try {
            orderRepository.saveAndFlush(order);
            orderRepository.delete(order);
            productService.increaseStock(order.getProduct() , order.getQuantity());
        }catch (OrderException e) {
            log.error("주문취소 중 에러가 발생하였습니다. : {}" ,e.getMessage());
            throw new CancelOrderException("주문취소 중 에러가 발생하였습니다.");
        }
    }

    public OrderDetailResponseDto getOneOrder(Long orderId) {
        Order order = getOrderById(orderId);
        return OrderDetailResponseDto.from(order);
    }

    public Page<OrderListResponseDto> getAllOrders(Pageable pageable , String username , Long getOrderId) {
        Page<Order> orders = orderRepository.search(username , getOrderId, pageable);
        return orders.map(OrderListResponseDto::from);
    }

    @Transactional
    public OrderDetailResponseDto completeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.completeOrder();
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderDetailResponseDto updateOrderStatus(Long orderId , OrderStatus orderStatus) {
        Order order = getOrderById(orderId);
        order.updateOrderStatus(orderStatus);
        return OrderDetailResponseDto.from(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );
    }
}

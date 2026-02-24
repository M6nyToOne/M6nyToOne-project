package sparta.m6nytooneproject.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.cart.service.CartService;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.exception.order.CancelOrderException;
import sparta.m6nytooneproject.global.exception.order.InvalidOrderStatusException;
import sparta.m6nytooneproject.global.exception.order.OrderException;
import sparta.m6nytooneproject.global.exception.order.OrderNotFoundException;
import sparta.m6nytooneproject.global.exception.user.UserRoleNotMatchException;
import sparta.m6nytooneproject.order.dto.OrderDetailResponseDto;
import sparta.m6nytooneproject.order.dto.OrderListResponseDto;
import sparta.m6nytooneproject.order.dto.OrderRequestByCsDto;
import sparta.m6nytooneproject.order.dto.OrderRequestByCustomerDto;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderSort;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.service.ProductService;
import sparta.m6nytooneproject.security.CustomUserDetails;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;
import sparta.m6nytooneproject.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;

    @Transactional
    public OrderDetailResponseDto createOrderByCustomer(OrderRequestByCustomerDto request, Long customerId, Long cartId) {
        User customer = userService.getUserById(customerId);
        Product validProduct = productService.checkProductStock(request.getProductId() , request.getQuantity());

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
        productService.decreaseStock(validProduct, request.getQuantity());
        cartService.deleteCart(cartId, customerId);

        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderDetailResponseDto createOrderByAdmin(OrderRequestByCsDto request, Long customerId , Long adminId) {
        User customer = userService.getUserById(customerId);
        if(!customer.getRole().equals(UserRole.CUSTOMER)) {
            throw new UserRoleNotMatchException("주문 대상이 고객이 아닙니다.");
        }
        User admin = userService.getUserById(adminId);
        Product validProduct = productService.checkProductStock(request.getProductId() , request.getQuantity());

        Order order = new Order(
                validProduct.getPrice(),
                request.getQuantity(),
                OrderStatus.PREPARED,
                validProduct.getProductName(),
                customer.getUserName(),
                validProduct,
                customer,
                admin
        );
        orderRepository.save(order);
        productService.decreaseStock(validProduct, request.getQuantity());
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, String cancelReason) {
        Order order = getOrderById(orderId);

        if(!order.getStatus().equals(OrderStatus.PREPARED)) {
            throw new InvalidOrderStatusException(order.getStatus());
        }

        order.setCancelOrder(cancelReason);

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

    public Page<OrderListResponseDto> getAllOrders(int page, int size, OrderSort orderSort, String username, OrderStatus orderStatus) {
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by(orderSort.getProperty()).descending());

        Page<Order> orders = orderRepository.searchByName(orderStatus, username, pageable);
        return orders.map(OrderListResponseDto::from);
    }

    public Page<OrderListResponseDto> getOrdersByCustomerId(int page, int size, OrderSort orderSort, Long customerId) {
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by(orderSort.getProperty()).descending());

        Page<Order> orders = orderRepository.searchByCustomerId(customerId, pageable);
        return orders.map(OrderListResponseDto::from);
    }

    @Transactional
    public OrderDetailResponseDto completeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.completeOrder();
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderDetailResponseDto updateOrderStatus(Long orderId) {
        Order order = getOrderById(orderId);
        order.updateOrderStatus();
        return OrderDetailResponseDto.from(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );
    }
}

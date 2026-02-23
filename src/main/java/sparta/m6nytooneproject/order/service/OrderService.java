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
import sparta.m6nytooneproject.global.exception.order.OrderException;
import sparta.m6nytooneproject.global.exception.order.OrderNotFoundException;
import sparta.m6nytooneproject.order.dto.OrderDetailResponseDto;
import sparta.m6nytooneproject.order.dto.OrderListResponseDto;
import sparta.m6nytooneproject.order.dto.OrderRequestByCsDto;
import sparta.m6nytooneproject.order.dto.OrderRequestByCustomerDto;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.service.ProductService;
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

        userService.validCustomer(customer.getRole());

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
        userService.validCustomer(customer.getRole());

        User admin = userService.getUserById(adminId);
        userService.validateIsAdmin(admin.getRole());

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

    public Page<OrderListResponseDto> getAllOrders(int page, int size, String username , Long getOrderId) {
        Pageable pageable = PageRequest.of(page + AuthConstants.PAGE_DEFAULT, size, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.search(username , getOrderId, pageable);
        return orders.map(OrderListResponseDto::from);
    }

    @Transactional
    public OrderDetailResponseDto completeOrder(Long orderId, UserRole userRole) {
        userService.validateIsAdmin(userRole);

        Order order = getOrderById(orderId);
        order.completeOrder();
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderDetailResponseDto updateOrderStatus(Long orderId , OrderStatus orderStatus, UserRole userRole) {
        userService.validateIsAdmin(userRole);

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

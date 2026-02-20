package sparta.m6nytooneproject.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.order.dto.OrderDetailResponseDto;
import sparta.m6nytooneproject.order.dto.OrderListResponseDto;
import sparta.m6nytooneproject.order.dto.OrderRequest;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Product checkProductStock(Long productId , int quantity) {
        // 했다치고
        return productRepository.findById(productId).orElseThrow(
                () ->  new RuntimeException("User not found")
        );
    }
    public User checkValidateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () ->  new RuntimeException("User not found")
        );
        // 했다치고
    }

    public void manageProductStock(Long productId , int quantity) {}

    @Transactional
    public OrderDetailResponseDto createOrder(OrderRequest orderRequest) {
        User admin = null;
        User customer = checkValidateUser(orderRequest.getUserId());
        Product product = checkProductStock(orderRequest.getProductId(),orderRequest.getQuantity());

        if (orderRequest.getAdminId() != null) {
            admin = checkValidateUser(orderRequest.getAdminId());
        }

        Order order = new Order(
                product.getPrice(),
                orderRequest.getQuantity(),
                OrderStatus.PREPARED,
                product.getProductName(),
                customer.getUserName(),
                product,
                customer,
                admin
        );
        orderRepository.save(order);
        //TODO : product 수량 조절 서비스 호출
        manageProductStock(orderRequest.getProductId(),orderRequest.getQuantity());

        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public void cancelOrder(Long orderId , String cancelReason) {
        Order order = getOrderById(orderId);
        order.cancelOrder(cancelReason);

        orderRepository.saveAndFlush(order);
        orderRepository.delete(order);

        //TODO : product 수량 조절 서비스 호출
        manageProductStock(order.getProduct().getId(),order.getQuantity());
    }

    public OrderDetailResponseDto getOrderDetail(Long orderId) {
        Order order = getOrderById(orderId);

        return OrderDetailResponseDto.from(order);
    }

    public Page<OrderListResponseDto> getAllOrders(Pageable pageable , String username , Long getOrderId) {
        // TODO : 세션 검증
        Page<Order> orders = orderRepository.search(username , getOrderId, pageable);
        return orders.map(OrderListResponseDto::from);
    }

    @Transactional
    public OrderDetailResponseDto completeOrderStatus(Long orderId) {
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
                () -> new RuntimeException("Order not found")
        );
    }
}

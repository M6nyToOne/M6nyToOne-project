package sparta.m6nytooneproject.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.order.dto.OrderRequest;
import sparta.m6nytooneproject.order.dto.OrderResponseDto;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.order.entity.OrderStatus;
import sparta.m6nytooneproject.order.repository.OrderRepository;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product checkProductStock(Long productId , int quantity) {
        // 했다치고
        return productRepository.findById(productId).orElseThrow(
                () ->  new RuntimeException("User not found")
        );
    }

    @Transactional(readOnly = true)
    public void checkValidateUser(String username) {
        // 했다치고
    }

    public void manageProductStock(Long productId , int quantity) {}

    @Transactional
    public OrderResponseDto createOrder(OrderRequest orderRequest) {
        checkValidateUser(orderRequest.getUserName());
        Product product = checkProductStock(orderRequest.getProductId(),orderRequest.getQuantity());

        Order order = new Order(
                product.getPrice(),
                orderRequest.getQuantity(),
                OrderStatus.PREPARED,
                product.getProductName(),
                orderRequest.getUserName(),
                product
        );

        orderRepository.save(order);

        //TODO : product 수량 조절 서비스 호출
        manageProductStock(orderRequest.getProductId(),orderRequest.getQuantity());

        return new OrderResponseDto(
                order.getId(),
                order.getProductName(),
                product.getPrice(),
                product.getPrice() * orderRequest.getQuantity()
        );
    }



    @Transactional
    public void cancelOrder(Long orderId , String cancelReason) {
        Order order = getOrderById(orderId);
        order.cancelOrder(cancelReason);

        //TODO : product 수량 조절 서비스 호출
        manageProductStock(order.getProduct().getId(),order.getQuantity());
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderDetail(Long orderId) {
        Order order = getOrderById(orderId);

        return new OrderResponseDto(
                order.getId(),
                order.getProductName(),
                order.getProductPrice(),
                order.getProductPrice() * order.getQuantity()
        );
    }

    public Page<OrderResponseDto> getAllOrders(Pageable pageable , String username , Long getOrderId) {
        // TODO : 세션 검증

        Page<Order> orders = orderRepository.search(username , getOrderId, pageable);
        return orders.map(OrderResponseDto::from);
    }

    @Transactional
    public OrderResponseDto completeOrderStatus(Long orderId) {
        Order order = getOrderById(orderId);

        order.completeOrder();

        return new OrderResponseDto(
                order.getId(),
                order.getProductName(),
                order.getProductPrice(),
                order.getProductPrice() * order.getQuantity()
        );
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId , String statusString) {
        Order order = getOrderById(orderId);
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(statusString);
            order.updateOrderStatus(orderStatus);
        }catch (RuntimeException e) {
            log.error("주문 상태 업데이트 에러 발생 {}", e.getMessage());
        }

        return new OrderResponseDto(
                order.getId(),
                order.getProductName(),
                order.getProductPrice(),
                order.getProductPrice() * order.getQuantity()
        );
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found")
        );
    }
}

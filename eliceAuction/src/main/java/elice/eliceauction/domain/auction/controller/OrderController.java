package elice.eliceauction.domain.auction.controller;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.auction.entity.OrderDto;
import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    // 주문 생성 API
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto request) {
        Order order = orderService.createOrder(request.getProductId(), request.getUserId(), request.getUserAddressId(), request.getPrice());
        return ResponseEntity.ok(order);
    }

    // 주문 저장 API
    @PostMapping("/save")
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        Order savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    // 주문 취소 API
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    // 특정 사용자의 주문 조회 API
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    // 특정 상품에 대한 주문 조회 API
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProduct(@PathVariable Long productId) {
        List<Order> orders = orderService.getOrdersByProduct(productId);
        return ResponseEntity.ok(orders);
    }

    // 주문 수정 API
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order.UpdateOrderRequest request) {
        Order updatedOrder = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(updatedOrder);
    }

    // 배송 정보 생성 API
    @PostMapping("/delivery")
    public ResponseEntity<UserAddress> createDeliveryInfo(@RequestParam String name, @RequestParam Long userId) {
        UserAddress userAddress = orderService.createDeliveryInfo(name, userId);
        return ResponseEntity.ok(userAddress);
    }

}

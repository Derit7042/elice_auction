package elice.eliceauction.domain.auction.controller;

import elice.eliceauction.domain.auction.entity.Order;
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
    private final UserRepository userRepository; // UserRepository 주입

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // 주문 생성
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestParam Long productId,
                                             @RequestParam Long userId,
                                             @RequestParam Long userAddressId,
                                             @RequestParam int price) {
        Order order = orderService.createOrder(productId, userId, userAddressId, price);
        return ResponseEntity.ok(order);
    }

    // 주문 저장
    @PostMapping("/save")
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        Order savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    // 배송 정보 생성
    @PostMapping("/create-delivery")
    public ResponseEntity<UserAddress> createDeliveryInfo(@RequestParam String name,
                                                          @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
        UserAddress userAddress = orderService.createDeliveryInfo(name, user.getId());
        return ResponseEntity.ok(userAddress);
    }


    // 특정 사용자의 주문 목록 가져오기
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    // 특정 상품에 대한 주문 목록 가져오기
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProduct(@PathVariable Long productId) {
        List<Order> orders = orderService.getOrdersByProduct(productId);
        return ResponseEntity.ok(orders);
    }
    //주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

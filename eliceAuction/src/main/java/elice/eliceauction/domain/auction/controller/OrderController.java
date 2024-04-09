package elice.eliceauction.domain.auction.controller;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.auction.entity.OrderDto;
import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }
    // 사용자 Id로 주문 목록 가져오기
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable("userId") Long userId) {
        try {
            // 특정 사용자의 주문 목록 가져오기
            List<Order> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (EntityNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 목록을 가져오는 중 오류가 발생했습니다.");
        }
    }

    // 현재 로그인한 사용자의 장바구니에 담긴 상품들을 주문
    @PostMapping("/order")
    public ResponseEntity<String> orderCartItems(@AuthenticationPrincipal User user) {
        List<CartItem> cartItems = cartService.getCarts(user);
        // 장바구니가 비어있는 경우
        if (cartItems.isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장바구니가 비어 있습니다. 상품을 추가해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니에 담긴 상품을 주문했습니다.");
    }

    // 주문 생성
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        try {
            // 주문 생성
            Order createdOrder = orderService.createOrder(orderDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (EntityNotFoundException e) {
            // 상품이나 사용자를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품, 사용자 또는 사용자 주소를 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 생성 중 오류가 발생했습니다.");
        }
    }



    //주문 주소 수정
    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(
            @PathVariable Long orderId,
            @RequestParam Long userAddressId
    ) {
        try {
            // 주문 수정
            Order updatedOrder = orderService.updateOrder(orderId, userAddressId);
            return ResponseEntity.ok(updatedOrder);
        } catch (EntityNotFoundException e) {
            // 주문을 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 주소를 수정하는 중 오류가 발생했습니다.");
        }
    }


    //주문 취소
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("ID가 " + orderId + "인 주문이 성공적으로 취소되었습니다.");
        } catch (EntityNotFoundException e) {
            // 주문을 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 취소 중 오류가 발생했습니다.");
        }
    }


    // 특정 상품에 대한 주문 목록 가져오기
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getOrdersByProduct(@PathVariable("productId") Long productId) {
        try {
            List<Order> orders = orderService.getOrdersByProduct(productId);
            return ResponseEntity.ok(orders);
        } catch (EntityNotFoundException e) {
            // 상품을 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 조회 중 오류가 발생했습니다.");
        }
    }
}

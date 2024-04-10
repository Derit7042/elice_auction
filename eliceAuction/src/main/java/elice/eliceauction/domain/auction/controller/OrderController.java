package elice.eliceauction.domain.auction.controller;

import elice.eliceauction.domain.auction.entity.*;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.entity.CartResponseDto;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public OrderController(OrderService orderService,UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }
    // 사용자 Id로 주문 목록 가져오기
    /*********스웨거 어노테이션**********/
    @Operation(summary = "사용자 ID로 주문 목록 조회", description = "유저 id(userId)를 이용하여 해당 유저의 상품 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "주문 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
    })
    @Parameter(name = "userId", description = "사용자의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable("userId") Long userId) {
        try {
            // 사용자 ID로 주문 목록 가져오기
            User user = userService.findUserById(userId);
            List<Order> orders = orderService.getOrdersByUser(user.getId());
            return ResponseEntity.ok(orders);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    // 주소 생성
    @PostMapping("/delivery/create")
    public ResponseEntity<?> createDeliveryInfo(
            @RequestBody DeliveryDto deliveryDto) {

        try {
            // 주소 생성
            UserAddress createdAddress = orderService.createDeliveryInfo(deliveryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
        } catch (EntityNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주소 생성 중 오류가 발생했습니다.");
        }
    }


    //주문 주소 수정
    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateOrderDto updateOrderDto) {

        try {
            // 주문 수정
            Order updatedOrder = orderService.updateOrder(updateOrderDto);
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
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody UpdateOrderDto updateOrderDto) {
        try {
            orderService.cancelOrder(updateOrderDto.getOrderId());
            return ResponseEntity.ok("ID가 " + updateOrderDto.getOrderId() + "인 주문이 성공적으로 취소되었습니다.");
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


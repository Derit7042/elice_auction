package elice.eliceauction.domain.auction.controller;

import elice.eliceauction.domain.auction.entity.*;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.service.MemberService;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.exception.auction.InvalidOrderException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final CartService cartService;
    private final ProductService productService;


    @Autowired
    public OrderController(OrderService orderService, MemberService memberService, CartService cartService, ProductService productService) {
        this.orderService = orderService;
        this.memberService = memberService;
        this.cartService = cartService;
        this.productService = productService;
    }
    // 사용자 Id로 주문 목록 가져오기
    /*********스웨거 어노테이션**********/
    @Operation(summary = "사용자 ID로 주문 목록 조회", description = "유저 id(memberId)를 이용하여 해당 유저의 상품 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "주문 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
    })
    @Parameter(name = "memberId", description = "사용자의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Order>> getOrdersByMember(@AuthenticationPrincipal UserDetails userDetails
                                                        ,@PathVariable("memberId") Long memberId) { // <<
        try {
            String username = userDetails.getUsername();
            Member member = memberService.findMemberByUsername(username);
            memberId = member.getId();

            // 사용자 ID로 주문 목록 가져오기
//            Member member = memberService.findMemberById(memberId);
            List<Order> orders = orderService.getOrdersByMember(memberId);
            return ResponseEntity.ok(orders);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 현재 로그인한 사용자의 장바구니에 담긴 상품들을 주문

    @PostMapping("/order")
    public ResponseEntity<?> orderCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        List<Order> orders = orderService.createOrdersFromCart((Member) userDetails);
        return ResponseEntity.ok().body("주문이 성공적으로 완료되었습니다.");
    }
    // 주문 생성 API
    @PostMapping("/member/create")
    public ResponseEntity<?> createOrderForLoggedInUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderDto orderDto) {
        try {
            Member member = memberService.findMemberByUsername(userDetails.getUsername());
            Product product = productService.show(orderDto.getProductId());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
            }
            Order order = orderService.createLoginOrder(member, product, orderDto.getMemberAddressId());
            return ResponseEntity.ok(order);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the order");
        }
    }

    // 주문 생성
    /*********스웨거 어노테이션**********/
    @Operation(summary = "회원 주문 생성", description = "회원이 상품을 주문합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "주문 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
    })
    @Parameter(name = "orderDto", description = "상품 주문시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
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
    /*********스웨거 어노테이션**********/
    @Operation(summary = "배송지 추가 입력", description = "회원이 배송지를 추가 입력합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "배송지 입력 성공",
                    content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
    })
    @Parameter(name = "deliveryDto", description = "배송지 생성시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
    @PostMapping("/delivery/create")
    public ResponseEntity<?> createDeliveryInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DeliveryDto deliveryDto) {

        try {
            // 주소 생성
            if (userDetails != null && userDetails.getUsername() != null) {
                String username = userDetails.getUsername();
                Member member = memberService.findMemberByUsername(username);
                Long memberId = member.getId();

                deliveryDto.setMemberId(memberId); // 회원 ID 설정
            }
            MemberAddress createdAddress = orderService.createDeliveryInfo(deliveryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
        }  catch (Exception e) {
            // 기타 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //주문 주소 수정
    /*********스웨거 어노테이션**********/
    @Operation(summary = "배송지 수정", description = "회원이 배송지를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "배송지 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdateOrderDto.class))),
    })
    @Parameter(name = "updateOrderDto", description = "배송지 수정시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateOrderDto updateOrderDto) {
        // 주문 수정
        Order updatedOrder = orderService.updateOrder(updateOrderDto);
        return ResponseEntity.ok(updatedOrder);
    }


    //주문 취소
    /*********스웨거 어노테이션**********/
    @Operation(summary = "주문 취소", description = "회원이 상품을 주문 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "주문 취소 성공",
                    content = @Content(schema = @Schema(implementation = UpdateOrderDto.class))),
    })
    @Parameter(name = "orderId", description = "주문 Id를 가지고 주문 취소 합니다.")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        String responseMessage = "주문 번호가 " + orderId + "인 주문이 취소되었습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    // 특정 상품에 대한 주문 목록 가져오기
    /*********스웨거 어노테이션**********/
    @Operation(summary = "상품 ID로 주문 목록 조회", description = "상품 id(product_id)를 이용하여 해당 주문 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "주문 조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
    })
    @Parameter(name = "productId", description = "상품의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getOrdersByProduct(@PathVariable("productId") Long productId) {
        List<Order> orders = orderService.getOrdersByProduct(productId);
        return ResponseEntity.ok(orders);
    }
}


package elice.eliceauction.domain.auction.controller;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.auction.entity.OrderDto;
import elice.eliceauction.domain.auction.entity.UpdateOrderDto;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.product.dto.ProductDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 모든 회원들의 주문 내역 조회
    /*********스웨거 어노테이션**********/
    @Operation(summary = "모든 회원들의 주문 내역 조회", description = "관리자가 모든 회원들의 주문 내역을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 목록 페이지 조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
    })
    /*********스웨거 어노테이션**********/
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // 사용자의 주문 내역에서 배송 상태 수정
    /*********스웨거 어노테이션**********/
    @Operation(summary = "회원의 주문 내역에서 배송 상태 수정", description = "관리자가 회원의 주문 내역에서 배송 상태를 수정한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "배송 상태 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdateOrderDto.class))),
    })
    @Parameter(name = "orderId", description = "주문 Id를 가지고 주문 내역을 조회합니다.")
    @Parameter(name = "updateOrderDto", description = "배송지 수정시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
    @PatchMapping("/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderDto updateOrderDto) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, updateOrderDto.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }

    // 회원들의 주문 내역 삭제
    /*********스웨거 어노테이션**********/
    @Operation(summary = "관리자가 회원의 주문 내역 삭제", description = "관리자가 회원의 주문 내역을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "주문 내역 삭제 성공",
                    content = @Content(schema = @Schema(implementation = UpdateOrderDto.class))),
    })
    @Parameter(name = "orderId", description = "관리자가 회원의 주문 아이디를 가지고 주문 내역을 삭제한다.")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("주문이 성공적으로 삭제되었습니다.");
    }
}

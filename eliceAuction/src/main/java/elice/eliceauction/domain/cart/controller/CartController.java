package elice.eliceauction.domain.cart.controller;

import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.entity.CartResponseDto;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.member.entity.User;
import elice.eliceauction.domain.member.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "cart", description = "장바구니 API")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(final CartService cartService, final UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }
    
    // 장바구니 목록 불러오기
    /*********스웨거 어노테이션**********/
    @Operation(summary = "장바구니 조회", description = "유저 id(userId)를 이용하여 해당 유저의 장바구니에 담긴 상품 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "장바구니 조회 성공",
                        content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
            })
    @Parameter(name = "userId", description = "사용자의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDto>> getCarts(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId);
        List<CartItem> cartItems = cartService.getCarts(user);

        // CartItem -> DTO로 변환
        List<CartResponseDto> cartResponseDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartResponseDtos.add(CartItem.toDto(cartItem));
        }

        return ResponseEntity.ok(cartResponseDtos);
    }
    
    // 장바구니에 상품 추가
    /*********스웨거 어노테이션**********/
    @Operation(summary = "장바구니에 상품 추가", description = "유저 id(userId)를 이용하여 해당 유저의 장바구니에 상품id(productId)에 해당하는 상품을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 추가 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
    })
    @Parameter(name = "userId", description = "사용자의 고유 id 번호")
    @Parameter(name = "productId", description = "상품 id")
    /*********스웨거 어노테이션**********/
    @PostMapping("/{userId}")
    public ResponseEntity<CartResponseDto> addCartItem(@PathVariable("userId") Long userId,
                                                       @RequestParam("productId") Long productId) {
        User user = userService.findUserById(userId);
        cartService.add(user, productId);
        CartItem cartItem = cartService.getCart(user, productId);

        // CartItem -> DTO로 변환
        CartResponseDto dto = CartItem.toDto(cartItem);

        return ResponseEntity.ok(dto);
    }
    
    // 장바구니에서 특정 상품 삭제
    /*********스웨거 어노테이션**********/
    @Operation(summary = "장바구니에서 상품 삭제", description = "유저 id(userId)를 이용하여 해당 유저의 장바구니에 상품id(productId)에 해당하는 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상품 삭제 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
    })
    @Parameter(name = "userId", description = "사용자의 고유 id 번호")
    @Parameter(name = "productId", description = "상품 id")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{userId}")
    public ResponseEntity<List<CartResponseDto>> deleteCartItem(@PathVariable("userId") Long userId,
                                                                @RequestParam("productId") List<Long> productId) {
        User user = userService.findUserById(userId);
        List<CartItem> deleted = new ArrayList<>();
        for (Long id : productId) {
            deleted.add(cartService.delete(user, id));
        }

        // CartItem -> DTO로 변환
        List<CartResponseDto> cartResponseDtos = new ArrayList<>();
        for (CartItem cartItem : deleted) {
            cartResponseDtos.add(CartItem.toDto(cartItem));
        }

        return ResponseEntity.ok(cartResponseDtos);
    }
}

package elice.eliceauction.domain.cart.controller;

import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCarts(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId);
        List<CartItem> cartItems = cartService.getCarts(user);

        return ResponseEntity.ok(cartItems);
    }
    
    // 장바구니에 상품 추가
    @PostMapping("/{userId}")
    public ResponseEntity<List<CartItem>> addCartItem(@PathVariable("userId") Long userId, @RequestParam("productId") Long productId) {
        User user = userService.findUserById(userId);
        cartService.add(user, productId);
        List<CartItem> cartItems = cartService.getCarts(user);

        return ResponseEntity.ok(cartItems);
    }
    
    // 장바구니에서 특정 상품 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<List<CartItem>> deleteCartItem(@PathVariable("userId") Long userId, @RequestParam("productId") List<Long> productId) {
        User user = userService.findUserById(userId);
        for (Long id : productId) {
            cartService.delete(user, id);
        }
        List<CartItem> cartItems = cartService.getCarts(user);

        return ResponseEntity.ok(cartItems);
    }
}

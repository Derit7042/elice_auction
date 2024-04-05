package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.repository.CartItemRepository;
import elice.eliceauction.domain.cart.repository.CartRepository;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }
    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCount(0);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCarts(User user) {
        return cartItemRepository.findAllByCart(getCartInfo(user));
    }

    @Override
    public CartItem getCart(User user, Long productId) {
        // TODO: 예외 만들기
        return cartItemRepository.findByCartAndProductId(getCartInfo(user), productId)
                .orElseThrow(() -> new RuntimeException("카트에 상품이 비어 있습니다."));
    }

    @Override
    public boolean isEmpty(User user) {
        return getCount(user) == 0 ? true : false;
    }

    @Override
    public void clear(User user) {
        cartItemRepository.deleteAllByCart(getCartInfo(user));
    }

    @Override
    public void add(User user, Long productId) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(getCartInfo(user));

        cartItem.setProduct(productService.show(productId));
    }

    @Override
    public CartItem delete(User user, Long productId) {

        // TODO: 예외 만들기
        return cartItemRepository.deleteByCartAndProductId(getCartInfo(user), productId)
                .orElseThrow(() -> new RuntimeException("상품을 삭제할 수 없습니다."));
    }

    @Override
    public int getCount(User user) {
        Cart cart = getCartInfo(user);

        return cart.getCount();
    }

    private Cart getCartInfo(User user){
        // TODO: 예외 만들기
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("유저의 장바구니가 존재하지 않습니다.") );
    }
}

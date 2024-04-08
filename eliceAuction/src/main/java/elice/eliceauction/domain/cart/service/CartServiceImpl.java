package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.repository.CartItemRepository;
import elice.eliceauction.domain.cart.repository.CartRepository;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.exception.cart.InvalidCartItemException;
import elice.eliceauction.exception.cart.InvalidCartException;
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
        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCarts(User user) {
        return cartItemRepository.findAllByCart(getCartInfo(user));
    }

    @Override
    public CartItem getCart(User user, Long productId) {

        return cartItemRepository.findByCartAndProductId(getCartInfo(user), productId)
                .orElseThrow(() -> new InvalidCartItemException());
    }

    @Override
    public boolean isEmpty(User user) {
        return getCount(user) == 0 ? true : false;
    }

    @Override
    public void clear(User user) {
        Cart cart = getCartInfo(user);
        cartItemRepository.deleteAllByCart(cart);
        cart.setCount(0);
    }

    @Override
    public void add(User user, Long productId) {
        CartItem cartItem = new CartItem();
        Cart cart =  getCartInfo(user);
        cart.setCount(cart.getCount() + 1);
        cartItem.setCart(getCartInfo(user));

        cartItem.setProduct(productService.show(productId));

        cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem delete(User user, Long productId) {

        Cart cart = getCartInfo(user);


        CartItem deleted =  cartItemRepository.findByCartAndProductId(getCartInfo(user), productId)
                .orElseThrow(() -> new InvalidCartItemException("삭제할 상품이 카드에 존재하지 않습니다."));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

        cart.setCount(cart.getCount() - 1);
        cartRepository.save(cart);
        return deleted;
    }

    @Override
    public int getCount(User user) {
        Cart cart = getCartInfo(user);

        return cart.getCount();
    }

    private Cart getCartInfo(User user){
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new InvalidCartException() );
    }
}

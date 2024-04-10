package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.repository.CartItemRepository;
import elice.eliceauction.domain.cart.repository.CartRepository;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.exception.cart.DuplicatedCartItemException;
import elice.eliceauction.exception.cart.InvalidCartItemException;
import elice.eliceauction.exception.cart.InvalidCartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setCount(0);
        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCarts(Member member) {
        return cartItemRepository.findAllByCart(getCartInfo(member));
    }

    @Override
    public CartItem getCart(Member member, Long productId) {

        return cartItemRepository.findByCartAndProductId(getCartInfo(member), productId)
                .orElseThrow(() -> new InvalidCartItemException());
    }

    @Override
    public boolean isEmpty(Member member) {
        return getCount(member) == 0 ? true : false;
    }

    @Override
    public void clear(Member member) {
        Cart cart = getCartInfo(member);
        cartItemRepository.deleteAllByCart(cart);
        cart.setCount(0);
    }

    @Override
    public void add(Member member, Long productId) {
        CartItem cartItem = new CartItem();
        Cart cart =  getCartInfo(member);
        
        // 상품 중복여부 확인
        cartItemRepository.findByCartAndProductId(cart, productId)
                .ifPresent(e ->{
                throw new DuplicatedCartItemException();
            }
        );

        cart.setCount(cart.getCount() + 1);
        cartItem.setCart(getCartInfo(member));

        cartItem.setProduct(productService.show(productId));

        cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem delete(Member member, Long productId) {

        Cart cart = getCartInfo(member);


        CartItem deleted =  cartItemRepository.findByCartAndProductId(getCartInfo(member), productId)
                .orElseThrow(() -> new InvalidCartItemException("삭제할 상품이 카드에 존재하지 않습니다."));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

        cart.setCount(cart.getCount() - 1);
        cartRepository.save(cart);
        return deleted;
    }

    @Override
    public int getCount(Member member) {
        Cart cart = getCartInfo(member);

        return cart.getCount();
    }

    private Cart getCartInfo(Member member){
        return cartRepository.findByMember(member)
                .orElseThrow(() -> new InvalidCartException() );
    }
}

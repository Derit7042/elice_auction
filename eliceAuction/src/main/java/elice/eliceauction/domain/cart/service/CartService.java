package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.user.entity.User;

import java.util.List;

public interface CartService {

    void createCart(User user);
    List<CartItem> getCarts(User user);

    CartItem getCart(User user, Long productId);

    boolean isEmpty(User user);

    void clear(User user);

    void add(User user, Long productId);

    CartItem delete(User user, Long productId);

    int getCount(User user);
}

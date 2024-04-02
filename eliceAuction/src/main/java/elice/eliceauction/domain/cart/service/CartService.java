package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.user.entity.User;

import java.util.List;

public interface CartService {

    void createCart(User user);
    List<Product> getCarts(User user);

    Product getCart(User user, Long productId);

    boolean isEmpty(User user);

    void clear(User user);

    void add(User user, Long productId);

    Product delete(User user, Long productId);

    int getCount(User user);
}

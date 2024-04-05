package elice.eliceauction.domain.cart.repository;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCart(Cart cart);
    Optional<CartItem> findByCartAndProductId(Cart cart, long productId);

    void deleteAllByCart(Cart cart);

    Optional<CartItem> deleteByCartAndProductId(Cart cart, Long productId);
}

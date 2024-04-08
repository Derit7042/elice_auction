package elice.eliceauction.domain.cart.repository;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCart(Cart cart);
    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);

    void deleteAllByCart(Cart cart);

    void deleteByCartIdAndProductId(Long cartId, Long productId);// 왜 얘는 void 타입임?
}

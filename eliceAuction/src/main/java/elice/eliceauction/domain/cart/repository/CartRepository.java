package elice.eliceauction.domain.cart.repository;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}

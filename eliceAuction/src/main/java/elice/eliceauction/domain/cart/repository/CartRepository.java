package elice.eliceauction.domain.cart.repository;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}

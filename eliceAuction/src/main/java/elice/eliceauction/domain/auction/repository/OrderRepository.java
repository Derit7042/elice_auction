package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getByItemId(Product product);
    List<Order> getOrderByDate(Product product);

}
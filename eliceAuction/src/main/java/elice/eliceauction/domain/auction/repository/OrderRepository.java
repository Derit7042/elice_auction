package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특정 사용자에 대한 주문 조회
    List<Order> findByUser(User user);

    // 특정 작품에 대한 주문 조회
    List<Order> findByProduct(Product product);

    // 주문 취소
    void deleteById(Long id);
}
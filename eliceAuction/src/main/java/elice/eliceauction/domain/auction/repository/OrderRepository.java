package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특정 사용자에 대한 주문 조회
    List<Order> findByUser(User user);

    // 특정 작품에 대한 주문 조회
    List<Order> findByProduct(Product product);

    // 모든 주문 조회
    List<Order> findAll();

    // 주문 취소
    void deleteById(Long id);
}

package elice.eliceauction.domain.product.repository;

import elice.eliceauction.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

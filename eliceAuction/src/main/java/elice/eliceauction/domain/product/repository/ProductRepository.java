package elice.eliceauction.domain.product.repository;

import elice.eliceauction.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    ArrayList<Product> findAll();
}

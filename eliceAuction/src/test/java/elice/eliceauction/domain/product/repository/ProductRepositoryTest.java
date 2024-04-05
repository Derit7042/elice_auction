package elice.eliceauction.domain.product.repository;

import elice.eliceauction.domain.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@SpringBootTest
class ProductRepositoryTest {
    ProductRepository productRepository;
    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Test
    void findAll() {

        Product product = new Product();
        product.setTitle("대동여지도");
        product.setBrief("김정호가 만든 대동여지도");
        product.setPrice(100_000_000_000L);
        product.setWatchCount(999L);
        product.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        productRepository.save(product);
        ArrayList<Product> result = productRepository.findAll();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(product.getTitle()).isEqualTo(result.get(0).getTitle());
        Assertions.assertThat(product.getBrief()).isEqualTo(result.get(0).getBrief());
        Assertions.assertThat(product.getPrice()).isEqualTo(result.get(0).getPrice());
        Assertions.assertThat(product.getWatchCount()).isEqualTo(result.get(0).getWatchCount());
        Assertions.assertThat(product.getDate()).isEqualTo(result.get(0).getDate());

        System.out.println("현재 시간은 " + product.getDate());
    }

}
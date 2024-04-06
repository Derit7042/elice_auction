package elice.eliceauction.domain.product.repository;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductRepositoryTest {
    ProductRepository productRepository;
    ProductService productService;

    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
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


    @Test
    void testWatchCountIncrement() {
        // 테스트용 상품 데이터 생성
        Product product = new Product();
        product.setTitle("고려청자");
        product.setBrief("고려시대에 만들어진 청자");
        product.setPrice(300_000_000_000L);
        product.setWatchCount(0L);
        product.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        productRepository.save(product);

        // 조회 전 조회수 확인
        Long initialWatchCount = product.getWatchCount();
        System.out.println("조회 전 조회수: " + initialWatchCount);

        // 조회 메서드 호출
        productService.show(product.getId()); // 1
        productService.show(product.getId()); // 2
        productService.show(product.getId()); // 3
        productService.show(product.getId()); // 4
        productService.show(product.getId()); // 5

        // 조회 후 조회수 확인
        Product updatedProduct = productRepository.findById(product.getId()).orElse(null);
        System.out.println("조회 후 조회수: " + updatedProduct.getWatchCount());

        // 조회 전 후의 조회수 비교
        assertEquals(5L, updatedProduct.getWatchCount());
    }

}
package elice.eliceauction.domain.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    void index() {
        // 1. 예상 데이터

        // 2. 실제 데이터
        productService.index();

        // 3. 비교 및 검증
    }
}
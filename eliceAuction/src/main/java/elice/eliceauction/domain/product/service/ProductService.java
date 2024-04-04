package elice.eliceauction.domain.product.service;

import elice.eliceauction.domain.product.dto.ProductDto;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // 상품 리포지토리 객체 주입


    public List<Product> index() {
        return productRepository.findAll();
    }

    public Product show(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product create(ProductDto dto) {
        Product product = dto.toEntity();
        if (product.getId() != null) {
            return null;
        }
        return productRepository.save(product);
    }

    public Product update(Long id, ProductDto dto) {
        // 1. DTO -> 엔티티 변환하기
        Product product = dto.toEntity();
        log.info("id: {}, product: {}", id, product.toString());

        // 2. 타깃 조회하기
        Product target = productRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리하기
        if (target == null || id != product.getId()) {
            // 400, 잘못된 요청 응답
            log.info("잘못된 요청 id: {}, product: {}", id, product.toString());
            return null;
        }

        // 4. 업데이트 및 정상 응답(200)하기
        target.patch(product);
        Product updated = productRepository.save(target);
        return updated;
    }

    public Product delete(Long id) {
        // 1. 대상 찾기
        Product target = productRepository.findById(id).orElse(null);

        // 2. 잘못된 요청 처리하기
        if (target == null) {
            return null;
        }

        // 3. 대상 삭제하기
        productRepository.delete(target);
        return target;
    }

}

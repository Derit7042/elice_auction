package elice.eliceauction.domain.product.service;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // DB 에 있는 상품의 이름 반환
    public String getProductTitleById(Product product) {
        return productRepository.findById(product.getId()).get().getTitle();
    }

    // DB 에 있는 상품에 대한 간단한 설명 반환
    public String getBriefById(Product product) {
        return productRepository.findById(product.getId()).get().getBrief();
    }

    // DB 에 있는 상품의 그림 링크 반환
    public String getPictureLinkById(Product product) {
        return productRepository.findById(product.getId()).get().getPictureLink();
    }

    // DB 에 있는 상품의 조회수 반환
    public Long getWatchCountById(Product product) {
        return productRepository.findById(product.getId()).get().getWatchCount();
    }

    // DB 에 있는 상품의 판매자 이름 반환
    public String getSellerById(User user) {
        return productRepository.findById(user.getId()).get().getName();
    }

    // DB 에 있는 상품의 구매자 이름 반환
    public String getBuyerById(User user) {
        return productRepository.findById(user.getId()).get().getName();
    }

    // DB 에 있는 상품의 가격 반환
    public Long getProductPriceById(Product product) {
        return productRepository.findById(product.getId()).get().getPrice();
    }

    // DB 에 있는 상품 구매자 주소 ID 반환
    public int getUserAddressIDById(UserAddress userAddress) {
        return productRepository.findById(userAddress.getId()).get().getId();
    }

    // DB 에 있는 상품 등록일 반환
    public LocalDateTime getEnrollDateById(Product product) {
        return productRepository.findById(product.getId()).get().getDate();
    }

}

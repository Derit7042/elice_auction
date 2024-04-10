package elice.eliceauction.domain.product.controller;

import elice.eliceauction.domain.product.dto.ProductDto;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/new")
    public String newProductForm() {
        return "products/new";
    }

    @PostMapping("/products/create")
    public String createProduct(ProductDto dto) {
        log.info(dto.toString());

        // 1. DTO를 엔티티로 변환
        Product product = dto.toEntity();
        log.info(product.toString());

        // 2. 리포지토리로 엔티티를 DB에 저장
        Product saved = productRepository.save(product);
        log.info(saved.toString());

        return "redirect:/products/" + saved.getId();
    }

    @GetMapping("/products/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        log.info("id = " + id);
        // 1. id를 조회해 데이터 가져오기
        Product productEntity = productRepository.findById(id).orElse(null);

        // 2. 모델에 데이터 등록하기
        model.addAttribute("product", productEntity);

        // 3. 뷰 페이지 반환하기
        return "products/show";
    }

    @GetMapping("/products")
    public String index(Model model) {

        // 1. DB에서 모든 Product 데이터 가져오기
        List<Product> productEntityList = productRepository.findAll();

        // 2. 가져온 Product 묶음을 모델에 등록하기
        model.addAttribute("productList", productEntityList);

        // 3. 사용자에게 보여 줄 뷰 페이지 설정하기
        return "products/index";
    }

    @GetMapping("/products/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        // 수정할 데이터 가져오기
        Product productEntity = productRepository.findById(id).orElse(null);

        // 모델에 데이터 등록하기
        model.addAttribute("product", productEntity);

        // 뷰 페이지 설정하기
        return "products/edit";
    }

    @PostMapping("/products/update")
    public String update(ProductDto dto) {
        log.info(dto.toString());
        // 1. DTO를 엔티티로 변환하기
        Product productEntity = dto.toEntity();
        log.info(productEntity.toString());

        // 2. 엔티티를 DB에 저장하기
        // 2-1. DB에서 기존 데이터 가져오기
        Product target = productRepository.findById(productEntity.getId()).orElse(null);

        // 2-2. 기존 데이터 값을 갱신하기
        if (target != null) {
            productRepository.save(productEntity); // 엔티티를 DB에 저장 (갱신)
        }

        // 3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/products/" + productEntity.getId();
    }

    @GetMapping("/products/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다!");

        // 1. 삭제할 대상 가져오기
        Product target = productRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 2. 대상 엔티티 삭제하기
        if (target != null) {
            productRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }

        // 3. 결과 페이지로 리다이렉트하기
        return "redirect:/products";
    }
}

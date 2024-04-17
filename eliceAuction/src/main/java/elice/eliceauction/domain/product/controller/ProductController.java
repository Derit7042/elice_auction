package elice.eliceauction.domain.product.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Product 도메인의 view 컨트롤러
@Slf4j
@Controller
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {

    @GetMapping
    public String productListPage() {
        return "product-list/product-list.html";
    }

    @GetMapping("/add")
    public String productAddPage() {
        return "product-add/product-add.html";
    }

    @GetMapping("/{id}")
    public String productDetailPage() {
        return "product-detail/product-detail.html";
    }
}
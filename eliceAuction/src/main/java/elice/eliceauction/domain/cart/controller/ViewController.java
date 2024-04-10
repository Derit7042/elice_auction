package elice.eliceauction.domain.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class ViewController {

//    TODO: HTML 파일 연결
    @GetMapping
    public String cartViewPage(Model model) {
        return "cart/cart.html";
    }
}

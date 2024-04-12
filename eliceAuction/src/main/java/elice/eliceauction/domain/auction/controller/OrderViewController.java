package elice.eliceauction.domain.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderViewController {
    @GetMapping
    public String orderViewPage(Model model) {
        return "order/order.html";
    }
}

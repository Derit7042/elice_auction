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
    @GetMapping("/complete")
    public String orderCompletePage(Model model) {
        return "order-complete/order-complete.html";
    }
    @GetMapping("/details")
    public String orderdetailPage(Model model) {
        return "account-orders/account-orders.html";
    }
}

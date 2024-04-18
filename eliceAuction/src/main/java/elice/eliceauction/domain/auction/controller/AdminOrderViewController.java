package elice.eliceauction.domain.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminOrderViewController {
    @GetMapping("/orders")
    public String orderView(Model model) {
        return "admin-orders/admin-orders.html";
    }
}

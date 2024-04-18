package elice.eliceauction.domain.member.controller;

import lombok.experimental.PackagePrivate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    @GetMapping("/register")
    public String registerP() {
        return "register/register.html";
    }

    @GetMapping("/login")
    public String loginP() {
        return  "login/login.html";
    }

    @GetMapping("/account")
    public String accountP() {
        return  "account/account.html";
    }

    @GetMapping("/account/orders")
    public String accountOrderP() {
        return  "account-orders/account-orders.html";
    }

    @GetMapping("/account/security")
    public String accountSecurityP() {
        return  "account-security/account-security.html";
    }

    @GetMapping("/account/signout")
    public String accountSignoutP() {
        return  "account-signout/account-signout.html";
    }

    @GetMapping("/admin")
    public String adminP() { return "admin/admin.html";}

}

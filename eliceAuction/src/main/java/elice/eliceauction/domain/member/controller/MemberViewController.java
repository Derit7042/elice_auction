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
}

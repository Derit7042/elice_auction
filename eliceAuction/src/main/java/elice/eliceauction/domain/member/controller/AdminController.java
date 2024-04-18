package elice.eliceauction.domain.member.controller;


import elice.eliceauction.domain.member.entity.Role;
import elice.eliceauction.domain.member.service.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/admin-check")
    public ResponseEntity<String> checkAdmin() {
        try {
            String username = SecurityUtil.getLoginUsername(); // 현재 로그인한 사용자의 사용자명을 가져옴
            Member member = memberService.findMemberByUsername(username);

            if (member.getRole() == Role.ADMIN) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking admin status");
        }
    }
}


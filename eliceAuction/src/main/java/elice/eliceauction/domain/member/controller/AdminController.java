package elice.eliceauction.domain.member.controller;


import elice.eliceauction.domain.member.entity.Role;
import elice.eliceauction.domain.member.service.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin-check")
    public ResponseEntity<String> checkAdmin() {
        try {
            String username = SecurityUtil.getLoginUsername(); // 현재 로그인한 사용자의 사용자명을 가져옴
            logger.info("관리자 접근 확인 중: 사용자명 = {}", username);
            Member member = memberService.findMemberByUsername(username);

            if (member.getRole() == Role.ADMIN) {
                logger.info("관리자 접근 허용: 사용자명 = {}", username);
                return ResponseEntity.ok("success");
            } else {
                logger.warn("관리자 접근 거부: 사용자명 = {}", username);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 거부됨");
            }
        } catch (Exception e) {
            logger.error("관리자 상태 확인 중 오류 발생: 사용자명 = {}, 오류 = {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자 상태 확인 중 오류 발생");
        }
    }
}

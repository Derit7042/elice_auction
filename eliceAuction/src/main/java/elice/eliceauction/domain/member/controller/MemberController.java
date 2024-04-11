package elice.eliceauction.domain.member.controller;

import elice.eliceauction.domain.member.dto.*;
import elice.eliceauction.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;



    @GetMapping("/login")
    public String loginP() {
        // 정적 리소스 폴더 내의 login.html 파일로 리다이렉션
        return "redirect:/login/login.html";
    }
    /**
     * 회원가입
     */
    @GetMapping("/register")
    public String registerP() {
        // 정적 리소스 폴더 내의 register.html 파일로 리다이렉션
        return "redirect:/register/register.html";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
    }

    /**
     * 회원정보수정
     */
    @PutMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void updateBasicInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
        memberService.update(memberUpdateDto);
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/member/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.checkPassword(),updatePasswordDto.toBePassword());
    }


    /**
     * 회원탈퇴
     */
    @DeleteMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.checkPassword());
    }


    /**
     * 회원정보조회
     */
    @GetMapping("/member/{id}")
    public ResponseEntity<MemberInfoDto> getInfo(@Valid @PathVariable("id") Long id) throws Exception {
        MemberInfoDto info = memberService.getInfo(id);
        return new ResponseEntity<MemberInfoDto>(info, HttpStatus.OK);
    }

    /**
     * 내정보조회
     */
    @GetMapping("/member")
    public ResponseEntity<MemberInfoDto> getMyInfo(HttpServletResponse response) throws Exception {

        MemberInfoDto info = memberService.getMyInfo();
        return new ResponseEntity<MemberInfoDto>(info, HttpStatus.OK);
    }

}
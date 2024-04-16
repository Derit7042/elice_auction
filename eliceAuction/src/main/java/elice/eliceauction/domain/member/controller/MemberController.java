package elice.eliceauction.domain.member.controller;

import elice.eliceauction.domain.member.dto.*;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.service.MemberService;
import elice.eliceauction.security.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;


    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDto memberLoginDto, HttpServletResponse response) {
        try {
            // 사용자 인증
            Optional<Member> memberOptional = memberService.authenticate(memberLoginDto.username(), memberLoginDto.password());
            if (memberOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"로그인 실패\"}");
            }

            // JWT 토큰 생성
            Member member = memberOptional.get();
            String accessToken = jwtService.createAccessToken(member.getUsername());
            String refreshToken = jwtService.createRefreshToken();

            // 리프레시 토큰 업데이트
            jwtService.updateRefreshToken(member.getUsername(), refreshToken);

            // 토큰 발행
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header("Refresh-Token", "Bearer " + refreshToken)
                    .body("{\"message\":\"로그인 성공\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"서버 에러 발생: " + e.getMessage() + "\"}");
        }
    }


    /**
     * 회원가입 페이지 리다이렉션 제거: API에서는 직접 페이지 리다이렉션이 일반적이지 않음
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return ResponseEntity.ok().body("{\"message\":\"회원가입 성공\"}");
    }
    /**
     * 회원정보수정
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateBasicInfo(@PathVariable("id") Long id, @Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
        memberService.update(memberUpdateDto);
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@PathVariable("id") Long id, @Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.checkPassword(), updatePasswordDto.toBePassword());
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable("id") Long id, @Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.checkPassword());
    }

    /**
     * 회원정보조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoDto> getInfo(@PathVariable("id") Long id) throws Exception {
        MemberInfoDto info = memberService.getInfo(id);
        return ResponseEntity.ok(info);
    }

    /**
     * 내정보조회
     */
    @GetMapping("/me")
    public ResponseEntity<MemberInfoDto> getMyInfo() throws Exception {
        MemberInfoDto info = memberService.getMyInfo();
        return ResponseEntity.ok(info);
    }
}
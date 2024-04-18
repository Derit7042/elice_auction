package elice.eliceauction.security.jwt.authentication;

import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.repository.MemberRepository;
import elice.eliceauction.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final String NO_CHECK_URL = "/api/members/login";

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProcessingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("필터 처리 시작: 요청 URI = {}", request.getRequestURI());

        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            log.info("로그인 URL 패스: 진행 중지");
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            log.info("유효한 리프레시 토큰 발견: 재발급 처리 시작");
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        log.info("액세스 토큰 검증 시도");
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(accessToken -> {
            log.info("유효한 액세스 토큰 발견: 사용자 인증 시도");
            jwtService.extractUsername(accessToken).ifPresent(username -> {
                memberRepository.findByUsername(username).ifPresent(member -> {
                    log.info("사용자 인증 성공: {}", username);
                    saveAuthentication(member);
                });
            });
        });

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        UserDetails user = User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authoritiesMapper.mapAuthorities(user.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        log.info("보안 컨텍스트에 인증 정보 저장: {}", member.getUsername());
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken).ifPresent(member -> {
            log.info("리프레시 토큰 재발급: {}", member.getUsername());
            jwtService.sendAccessToken(response, jwtService.createAccessToken(member.getUsername()));
        });
    }


}
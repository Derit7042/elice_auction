package elice.eliceauction.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private long accessTokenValidityInSeconds;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;

    @Override
    public String createAccessToken(String username) {
        String token = JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000))
                .withClaim(USERNAME_CLAIM, username)
                .sign(Algorithm.HMAC512(secret));
        log.debug("액세스 토큰 생성: username={}, token={}", username, token);
        return token;
    }

    @Override
    public String createRefreshToken() {
        String token = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000))
                .sign(Algorithm.HMAC512(secret));
        log.debug("리프레시 토큰 생성: token={}", token);
        return token;
    }

    @Override
    public void updateRefreshToken(String username, String refreshToken) {
        memberRepository.findByUsername(username).ifPresentOrElse(
                member -> {
                    member.updateRefreshToken(refreshToken);
                    log.debug("리프레시 토큰 갱신: username={}, refreshToken={}", username, refreshToken);
                },
                () -> {
                    RuntimeException exception = new RuntimeException("해당 사용자를 찾을 수 없습니다.");
                    log.error("리프레시 토큰 갱신 실패: 존재하지 않는 사용자입니다. username={}", username, exception);
                    throw exception;
                }
        );
    }

    @Override
    public void destroyRefreshToken(String username) {
        memberRepository.findByUsername(username).ifPresentOrElse(
                member -> {
                    member.destroyRefreshToken();
                    log.debug("리프레시 토큰 파괴: username={}", username);
                },
                () -> {
                    RuntimeException exception = new RuntimeException("해당 사용자를 찾을 수 없습니다.");
                    log.error("리프레시 토큰 파괴 실패: 존재하지 않는 사용자입니다. username={}", username, exception);
                    throw exception;
                }
        );
    }

    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.debug("액세스 및 리프레시 토큰 응답 전송 완료.");
    }

    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        log.debug("액세스 토큰 응답 전송 완료.");
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(accessHeader);
        if (header == null) {
            log.error("액세스 토큰을 포함한 {} 헤더가 요청에 없습니다.", accessHeader);
            return Optional.empty();
        }
        if (header.startsWith(BEARER)) {
            String token = header.substring(BEARER.length()).trim();
            if (token.split("\\.").length == 3) {
                try {
                    Base64.getUrlDecoder().decode(token.split("\\.")[1]);  // Decoding to check if it's URL-safe
                    log.debug("액세스 토큰 추출 성공: token={}", token);
                    return Optional.of(token);
                } catch (IllegalArgumentException e) {
                    log.error("잘못된 Base64 인코딩: ", e);
                }
            } else {
                log.error("잘못된 형식의 토큰: {}", token);
            }
        } else {
            log.error("{} 헤더는 'Bearer '로 시작해야 합니다.", accessHeader);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        String header = request.getHeader(refreshHeader);
        if (header == null) {
            log.error("리프레시 토큰을 포함한 {} 헤더가 요청에 없습니다.", refreshHeader);
            return Optional.empty();
        }
        if (header.startsWith(BEARER)) {
            String refreshToken = header.substring(BEARER.length()).trim();
            log.debug("리프레시 토큰 추출 성공: refreshToken={}", refreshToken);
            return Optional.of(refreshToken);
        } else {
            log.error("{} 헤더는 'Bearer '로 시작해야 합니다.", refreshHeader);
        }
        return Optional.empty();
    }


    @Override
    public Optional<String> extractUsername(String accessToken) {
        try {
            String username = JWT.require(Algorithm.HMAC512(secret)).build().verify(accessToken).getClaim(USERNAME_CLAIM).asString();
            log.debug("토큰에서 사용자 이름 추출: username={}", username);
            return Optional.ofNullable(username);
        } catch (TokenExpiredException e) {
            log.error("토큰이 만료되었습니다: ", e);
        } catch (JWTVerificationException e) {
            log.error("토큰 검증 실패: ", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            log.debug("토큰 검증 성공: token={}", token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, "Bearer " + accessToken);
        log.debug("액세스 토큰 헤더 설정: {}", accessToken);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, "Bearer " + refreshToken);
        log.debug("리프레시 토큰 헤더 설정: {}", refreshToken);
    }
}

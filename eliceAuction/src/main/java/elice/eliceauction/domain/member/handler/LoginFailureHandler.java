package elice.eliceauction.domain.member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드로 변경

        // 사용자에게 보다 구체적인 에러 메시지를 제공
        response.getWriter().write("로그인에 실패하였습니다. 아이디와 비밀번호를 확인해 주세요.");

        // 실패한 사용자의 IP 주소와 실패 원인을 로그에 기록
        String clientIP = request.getRemoteAddr();
        log.info("로그인 실패 IP: {}, 이유: {}", clientIP, exception.getMessage());
    }
}

package elice.eliceauction.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 활성화
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // spring security는 암호화위해 BCrypt Encoder제공하고 권장한다.
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스들이 보안필터를 거치지 않게끔
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/font/**");
        //예외처리하고 싶은 url
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers( "/api/users/login","/api/users/register").permitAll()// 회원가입 및 로그인 페이지는 인증 없이 접근 가능
                    .requestMatchers("/admin").hasRole("ADMIN") //admin 페이지는 admin(관리자계정) 한해서 접근 가능
                                .requestMatchers("/myPage/**").hasAnyRole("ADMIN", "REGULAR") //마이페이지에 대한 접근허용(관리자, 일반회원)
                    .anyRequest().authenticated() // 그 외 모든 요청은 로그인 인증 필요
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login") // 로그인페이지가 어디인지 설정
                        .loginProcessingUrl("/login") // 로그인 데이터 처리할 post 경로 설정
                        .permitAll() //로그인 경로를 모두에게 오픈
                ); //

        http
                .csrf((auth) -> auth.disable()
                        );
        return http.build();
    }



}

package elice.eliceauction.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) //메서드 레벨에서 보안을 추가
@EnableWebSecurity  // Spring Security 활성화
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // spring security는 암호화위해 BCrypt Encoder제공하고 권장한다.
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/auth/**").permitAll()// 회원가입 및 로그인 페이지는 인증 없이 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN") //admin 페이지는 admin(관리자계정) 한해서 접근 가능
                        .requestMatchers("/myPage/**").hasAnyRole("ADMIN", "REGULAR") //마이페이지에 대한 접근허용(관리자, 일반회원)
                        .anyRequest().authenticated() // 그 외 모든 요청은 로그인 인증 필요
                );

        http
                .formLogin((auth) -> auth
//                      .loginPage("/api/users/login") // 로그인페이지가 어디인지 설정
                                .defaultSuccessUrl("/home") //로그인 성공 시 리다이렉트할 경로
                                .failureUrl("/login?error=ture") //로그인 실패 시 리다이렉트할 경로
                                .loginProcessingUrl("/api/auth/login") // 로그인 데이터 처리할 post 경로 설정
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .permitAll() //로그인 경로를 모두에게 오픈
                ); //
        http
                .logout()
                .logoutSuccessUrl("/login?logout") //로그아웃 성공 시 리다이렉트할 경로
                .permitAll();

        http
                .csrf((auth) -> auth.disable()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // 프론트엔드 호스팅 주소
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

}

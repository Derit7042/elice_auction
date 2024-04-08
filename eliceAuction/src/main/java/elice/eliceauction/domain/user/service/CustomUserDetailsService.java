package elice.eliceauction.domain.user.service;

import elice.eliceauction.domain.user.dto.CustomUserDetails;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
//사용자 인증 과정에서 사용자의 상세 정보를 불러오는 데 사용

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override  //제공된 이메일을 기반으로 사용자 정보를 데이터베이스에서 조회
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일로 사용자를 찾을 수 없습니다: " + email));
        //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
        return new CustomUserDetails(user);
    }
}

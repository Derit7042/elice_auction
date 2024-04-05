package elice.eliceauction.domain.user.service;

import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.entity.UserGrade;
import elice.eliceauction.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUp(User user) {
        // 사용자 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 사용자 정보 저장
        userRepository.save(user);
    }

    @Override
    public User findUser(Long userId) {
        // 사용자 ID로 사용자 정보 조회
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));
    }

    @Override
    public void updateUserInfo(User user) {
        // 사용자 정보 업데이트를 위해 먼저 해당 사용자가 존재하는지 확인
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (!existingUser.isPresent()) {
            throw new RuntimeException("회원정보를 찾을 수 없습니다.");
        }

        User updatedUser = existingUser.get();
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setGrade(user.getGrade());
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        // 사용자 ID로 사용자 삭제
        userRepository.deleteById(userId);
    }

    @Override
    public void changeUserGrade(Long userId, String newGrade) {
        // 사용자 등급 변경
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));
        user.setGrade(UserGrade.valueOf(newGrade));
        userRepository.save(user);
    }
}
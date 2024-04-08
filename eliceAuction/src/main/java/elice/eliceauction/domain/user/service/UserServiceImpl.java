package elice.eliceauction.domain.user.service;

import elice.eliceauction.domain.user.dto.UserRegistrationDto;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.entity.UserGrade;
import elice.eliceauction.domain.user.exception.UserAlreadyExistException;
import elice.eliceauction.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    // 모든 사용자 조회
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }

    @Override
    public User findUserById(Long userId) {
        // 사용자 ID로 사용자 정보 조회
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));
    }

    //사용자 정보 업데이트
    @Override
    public void updateUser(User user) {
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

    // 사용자 ID로 사용자 삭제
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + userId));
        userRepository.delete(user);
    }


    // 사용자 등록
    @Override
    public User registerNewUserAccount(UserRegistrationDto registrationDto) throws UserAlreadyExistException {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("이 이메일은 이미 존재합니다: " + registrationDto.getEmail());
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setGrade(UserGrade.REGULAR); // 사용자 등급 설정, 기본값은 "REGULAR"
        return userRepository.save(user);
    }

    // 사용자 등급을 ADMIN으로 업데이트 (승인)
    @Override
    public User approveAdmin(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setGrade(UserGrade.ADMIN); // 사용자 등급을 ADMIN으로 업데이트
            return userRepository.save(user);
        }).orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + userId));
    }
}
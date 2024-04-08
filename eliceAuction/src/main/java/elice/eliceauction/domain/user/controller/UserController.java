package elice.eliceauction.domain.user.controller;

import elice.eliceauction.domain.user.dto.UserRegistrationDto;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.exception.UserAlreadyExistException;
import elice.eliceauction.domain.user.repository.UserRepository;
import elice.eliceauction.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 모든 사용자 조회 - 관리자만 가능
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 'ADMIN' 역할을 가진 사용자만 접근 가능
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userDto) {
        try {
            User registeredUser = userService.registerNewUserAccount(userDto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            // 명확한 예외 처리
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND); // 404 Not Found
        }
        return new ResponseEntity<>(user, HttpStatus.OK); // 200 OK
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return new ResponseEntity<>("회원정보가 업데이트되었습니다.", HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 사용자를 찾을 수 없기 때문에 발생
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("회원정보가 삭제되었습니다.", HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 사용자를 찾을 수 없기 때문에 발생
        }
    }
}

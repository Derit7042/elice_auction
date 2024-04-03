package elice.eliceauction.domain.user.controller;

import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        userService.signUp(user);
        return new ResponseEntity<>("회원등록 성공입니다.", HttpStatus.CREATED);//201 Created
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        User user = userService.findUser(userId);
        if (user == null) {
            return new ResponseEntity<>("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND); // 404 Not Found
        }
        return new ResponseEntity<>(user, HttpStatus.OK); // 200 OK
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            userService.updateUserInfo(user);
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

    @PostMapping("/changeGrade")
    public ResponseEntity<?> changeUserGrade(@RequestParam Long userId, @RequestParam String newGrade) {
        try {
            userService.changeUserGrade(userId, newGrade);
            return new ResponseEntity<>("회원등급이 변경되었습니다.", HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 잘못된 요청이나 유효하지 않은 입력 때문에 발생
        }
    }
}

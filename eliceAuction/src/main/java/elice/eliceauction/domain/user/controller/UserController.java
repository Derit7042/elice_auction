package elice.eliceauction.domain.user.controller;

import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok("회원등록 성공입니다.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.findUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userService.updateUserInfo(user);
        return ResponseEntity.ok("회원정보가 업데이트되었습니다.");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("회원정보가 삭제되었습니다.");
    }

    @PostMapping("/changeGrade")
    public ResponseEntity<?> changeUserGrade(@RequestParam Long userId, @RequestParam String newGrade) {
        userService.changeUserGrade(userId, newGrade);
        return ResponseEntity.ok("회원등급이 변경되었습니다.");
    }


}

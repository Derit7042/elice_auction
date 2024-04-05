package elice.eliceauction.domain.user.service;

import elice.eliceauction.domain.user.dto.UserRegistrationDto;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    void signUp(UserRegistrationDto userDto);
    User findUser(Long userId);
    void updateUserInfo(User user);
    void deleteUser(Long userId);
    void changeUserGrade(Long userId, String newGrade);
}

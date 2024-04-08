package elice.eliceauction.domain.user.service;

import elice.eliceauction.domain.user.dto.UserRegistrationDto;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    void updateUser(User user);
    void deleteUser(Long userId); //회원탈퇴

    User login(String email, String password);

    List<User> findAllUsers();

    User registerNewUserAccount(UserRegistrationDto userDto);
    User approveAdmin(Long userId);

    User findUserById(Long userId);
}

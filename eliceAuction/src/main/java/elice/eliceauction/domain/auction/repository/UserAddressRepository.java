package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> getAllByUser(User user); // 메서드 이름 수정
    void deleteById(Long id); // 메서드 이름 수정
    UserAddress getById(Long id); // 메서드 이름 수정
    List<UserAddress> getAllByUserId(Long id);
}
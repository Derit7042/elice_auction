package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> getByUserId(User user);
    void deleteId(Long id);
    UserAddress getId(Long id);
    List<UserAddress> getAllByUserId(Long id);
}
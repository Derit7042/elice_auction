package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    // ID로 주소 삭제
    void deleteById(Long id);

    // ID로 주소 가져오기
    UserAddress getById(Long id);

    // 사용자 ID로 주소 찾기
    UserAddress findByUserId(Long userId);
}
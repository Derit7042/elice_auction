package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    // ID로 주소 삭제
    void deleteById(Long id);

    // 사용자 ID로 주소 찾기
    UserAddress findByUserId(Long userId);
}
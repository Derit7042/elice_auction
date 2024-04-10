package elice.eliceauction.domain.auction.repository;

import elice.eliceauction.domain.auction.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    // ID로 주소 삭제
    void deleteById(Long id);

    // 사용자 ID로 주소 찾기
    MemberAddress findByMemberId(Long memberId);
}
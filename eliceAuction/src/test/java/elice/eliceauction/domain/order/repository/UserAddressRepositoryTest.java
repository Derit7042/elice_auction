package elice.eliceauction.domain.order.repository;

import elice.eliceauction.domain.auction.entity.MemberAddress;
import elice.eliceauction.domain.auction.repository.MemberAddressRepository;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberAddressRepositoryTest {

    @Autowired
    private MemberAddressRepository memberAddressRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByMemberId() {
        // Given
        Member member = new Member();
        member.setMembername("송호진");
        member.setEmail("0918syj@gmail.com");
        member.setPassword("1234");
        memberRepository.save(member); // 사용자 저장

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setName("집");
        memberAddress.setAddress("광주광역시 광산구임당");
        memberAddress.setMember(member);
        memberAddressRepository.save(memberAddress); // 주소 저장

        // When
        MemberAddress foundAddress = memberAddressRepository.findByMemberId(member.getId());

        // Then
        assertNotNull(foundAddress); // null값이 아닌지 확인
        assertEquals(member.getId(), foundAddress.getMember().getId()); // 사용자 아이디가 같은지 확인
        assertEquals("집", foundAddress.getName()); // 이름이 같은지 확인
        assertEquals("광주광역시 광산구임당", foundAddress.getAddress()); // 주소가 같은지 확인
    }
    @Test
    void deleteById() {
        // Given
        Member member = new Member(); // 새로운 사용자 생성
        member.setMembername("송호진");
        member.setEmail("0918syj@gmail.com");
        member.setPassword("1234");
        memberRepository.save(member); // 사용자 저장

        MemberAddress memberAddress = new MemberAddress(); // 새로운 사용자 주소 생성
        memberAddress.setName("집");
        memberAddress.setAddress("광주광역시 광산구임당");
        memberAddress.setMember(member);
        memberAddressRepository.save(memberAddress); // 주소 저장

        // When (실행)
        memberAddressRepository.deleteById(memberAddress.getId()); // 주소 ID로 삭제 수행

        // Then (결과 확인)
        assertNull(memberAddressRepository.findByMemberId(member.getId())); // 삭제된 주소를 다시 조회하여 null이 반환되는지 확인
    }
}

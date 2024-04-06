package elice.eliceauction.domain.order.repository;

import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.auction.repository.UserAddressRepository;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserAddressRepositoryTest {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId() {
        // Given
        User user = new User();
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        userRepository.save(user); // 사용자 저장

        UserAddress userAddress = new UserAddress();
        userAddress.setName("집");
        userAddress.setAddress("광주광역시 광산구임당");
        userAddress.setUser(user);
        userAddressRepository.save(userAddress); // 주소 저장

        // When
        UserAddress foundAddress = userAddressRepository.findByUserId(user.getId());

        // Then
        assertNotNull(foundAddress); // null값이 아닌지 확인
        assertEquals(user.getId(), foundAddress.getUser().getId()); // 사용자 아이디가 같은지 확인
        assertEquals("집", foundAddress.getName()); // 이름이 같은지 확인
        assertEquals("광주광역시 광산구임당", foundAddress.getAddress()); // 주소가 같은지 확인
    }
    @Test
    void deleteById() {
        // Given
        User user = new User(); // 새로운 사용자 생성
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        userRepository.save(user); // 사용자 저장

        UserAddress userAddress = new UserAddress(); // 새로운 사용자 주소 생성
        userAddress.setName("집");
        userAddress.setAddress("광주광역시 광산구임당");
        userAddress.setUser(user);
        userAddressRepository.save(userAddress); // 주소 저장

        // When (실행)
        userAddressRepository.deleteById(userAddress.getId()); // 주소 ID로 삭제 수행

        // Then (결과 확인)
        assertNull(userAddressRepository.findByUserId(user.getId())); // 삭제된 주소를 다시 조회하여 null이 반환되는지 확인
    }
}

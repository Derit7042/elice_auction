package elice.eliceauction.domain.order.repository;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.auction.repository.OrderRepository;
import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void findByUser() {
        // Given
        // 테스트용 사용자 생성
        User user = new User();
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234"); // 사용자 비밀번호 설정

        // 테스트용 사용자 주소 생성
        UserAddress userAddress = new UserAddress();
        userAddress.setName("집");
        userAddress.setAddress("광주광역시, 광산구임당");
        userAddress.setUser(user);

        // 테스트용 상품 생성 및 저장
        Product product = new Product();
        product.setTitle("명품 시계");
        product.setBrief("존나 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(100L);
        productRepository.save(product); // Product 엔티티를 저장합니다.

        // 사용자 저장
        userRepository.save(user);

        // 테스트용 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setUserAddress(userAddress);
        order.setDate(LocalDateTime.now()); // 현재 날짜와 시간 설정

        // 주문 저장
        orderRepository.save(order);

        //When
        // 주문 조회
        List<Order> ordersByUser = orderRepository.findByUser(user);


        //Then
        // 조회된 주문이 null이 아니고, 리스트가 비어있지 않은지 확인
        assertNotNull(ordersByUser);
        assertEquals(1, ordersByUser.size());

        // 조회된 주문의 첫 번째 주문이 예상대로인지 확인
        Order retrievedOrder = ordersByUser.get(0);

        assertEquals(user, retrievedOrder.getUser());
        assertEquals(product.getId(), retrievedOrder.getProduct().getId()); // 제품 아이디 비교
        assertEquals(userAddress, retrievedOrder.getUserAddress());
    }

    @Test
    void findByProduct() {
        // Given
        // 테스트용 사용자 생성
        User user = new User();
        user.setUsername("호호진");
        user.setEmail("0918syj@naver.com");
        user.setPassword("5678"); // 사용자 비밀번호 설정

        // 테스트용 사용자 주소 생성
        UserAddress userAddress = new UserAddress();
        userAddress.setName("회사");
        userAddress.setAddress("광주광역시, 광산구임당");
        userAddress.setUser(user);

        // 테스트용 상품 생성 및 저장
        Product product = new Product();
        product.setTitle("명품 시계");
        product.setBrief("존나 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(100L);
        productRepository.save(product);

        // 사용자 저장
        userRepository.save(user);

        // 테스트용 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setUserAddress(userAddress);
        order.setDate(LocalDateTime.now()); // 현재 날짜와 시간 설정

        // 주문 저장
        orderRepository.save(order);

        // When
        // 특정 상품에 대한 주문 조회
        List<Order> ordersByProduct = orderRepository.findByProduct(product);

        // Then
        // 조회된 주문이 null이 아니고, 리스트가 비어있지 않은지 확인
        assertNotNull(ordersByProduct);
        assertEquals(1, ordersByProduct.size());

        // 조회된 주문의 첫 번째 주문이 예상대로인지 확인
        Order retrievedOrder = ordersByProduct.get(0);
        assertEquals(user, retrievedOrder.getUser());
        assertEquals(product.getId(), retrievedOrder.getProduct().getId()); // 제품 아이디 비교
        assertEquals(userAddress, retrievedOrder.getUserAddress());
    }


    @Test
    void deleteByIdTest() {
        // Given
        User user = createUser(); // 사용자 생성
        Product product = createProduct(); // 상품 생성
        Order order = createOrder(user, product); // 주문 생성
        Long orderId = order.getId(); // 주문의 ID를 가져옵니다.

        // When
        orderRepository.deleteById(orderId); // 주문 삭제

        // Then
        assertNull(orderRepository.findById(orderId).orElse(null)); // 삭제된 주문을 검색하여 없어야 합니다.
    }

    // 사용자 생성
    private User createUser() {
        User user = new User();
        user.setUsername("진진진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        return userRepository.save(user);
    }

    // 상품 생성
    private Product createProduct() {
        Product product = new Product();
        product.setTitle("명품 지갑");
        product.setBrief("살짝 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(100L);
        return productRepository.save(product);
    }

    // 주문 생성
    private Order createOrder(User user, Product product) {
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setDate(LocalDateTime.now());
        return orderRepository.save(order);
    }


}


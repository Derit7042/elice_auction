package elice.eliceauction.domain.order.service;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.auction.entity.OrderDto;
import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.auction.repository.OrderRepository;
import elice.eliceauction.domain.auction.repository.UserAddressRepository;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import elice.eliceauction.domain.member.entity.User;
import elice.eliceauction.domain.member.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testCreateOrder() {
        // Given
        Product product = new Product();
        product.setTitle("명품시계");
        product.setBrief("조금 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(1L);
        productRepository.save(product);

        User user = new User();
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        userRepository.save(user);

        UserAddress userAddress = new UserAddress();
        userAddress.setName("집");
        userAddress.setAddress("광주광역시");
        userAddress.setUser(user);
        userAddressRepository.save(userAddress);

        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(product.getId());
        orderDto.setUserId(user.getId());
        orderDto.setUserAddressId(userAddress.getId());

        // When
        Order createdOrder = orderService.createOrder(orderDto);

        // Then
        assertNotNull(createdOrder);
        assertNotNull(createdOrder.getId());
        assertEquals(product.getId(), createdOrder.getProduct().getId());
        assertEquals(user.getId(), createdOrder.getUser().getId());
        assertEquals(userAddress.getId(), createdOrder.getUserAddress().getId());
    }

    @Test
    public void testSaveOrder() {
        // Given
        Product product = new Product();
        product.setTitle("명품시계");
        product.setBrief("조금 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(1L);
        productRepository.save(product);

        Order order = new Order();
        order.setProduct(product);

        // When
        Order savedOrder = orderService.saveOrder(order);

        // Then
        // 저장된 Order 객체의 ID가 null이 아닌지 확인
        assertNotNull(savedOrder.getId());
        // 저장된 Order 객체를 예상과 일치하는지 확인
        Order retrievedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertNotNull(retrievedOrder);
        assertEquals(savedOrder.getId(), retrievedOrder.getId());
        // 저장된 Order 객체와 연결된 Product 정보가 일치하는지 확인
        assertEquals(product, savedOrder.getProduct());
    }

    @Test
    public void testCancelOrder() {
        // Given
        Product product = new Product();
        product.setTitle("명품시계");
        product.setBrief("조금 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(1L);
        productRepository.save(product);

        User user = new User();
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        userRepository.save(user);

        UserAddress userAddress = new UserAddress();
        userAddress.setName("집");
        userAddress.setAddress("광주광역시");
        userAddress.setUser(user);
        userAddressRepository.save(userAddress);

        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(product.getId());
        orderDto.setUserId(user.getId());
        orderDto.setUserAddressId(userAddress.getId());

        // When
        Order createdOrder = orderService.createOrder(orderDto);
        // When
        orderService.cancelOrder(createdOrder.getId());

        // Then
        // 주문이 삭제되었는지 확인
        assertFalse(orderRepository.existsById(createdOrder.getId()));
    }

    @Test
    public void testUpdateOrder() {
        // Given
        // 새로운 상품, 사용자, 사용자 주소 정보 설정
        Product product = new Product();
        product.setTitle("명품시계");
        product.setBrief("조금 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(1L);
        productRepository.save(product);

        User user = new User();
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        userRepository.save(user);

        UserAddress userAddress1 = new UserAddress();
        userAddress1.setName("집");
        userAddress1.setAddress("광주광역시");
        userAddress1.setUser(user);
        userAddressRepository.save(userAddress1);

        UserAddress userAddress2 = new UserAddress();
        userAddress2.setName("회사");
        userAddress2.setAddress("부산광역시");
        userAddress2.setUser(user);
        userAddressRepository.save(userAddress2);

        // 주문 생성
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 및 시간 가져오기
        Order order = new Order(product, user, userAddress1);
        order.setDate(now); // 주문 생성 시 현재 날짜 및 시간 설정
        orderRepository.save(order);

        // When
        // 주문 수정
        Order updatedOrder = orderService.updateOrder(order.getId(), userAddress2.getId());

        // Then
        assertNotNull(updatedOrder);
        // 수정된 주소 확인
        assertEquals(userAddress2.getId(), updatedOrder.getUserAddress().getId());
        // 주문 시간이 올바르게 설정되었는지 확인
        assertEquals(now, updatedOrder.getDate());
    }
    @Test
    public void testCreateDeliveryInfo() {
        // Given
        String name = "집";
        String address = "광주광역시";
        User user = new User();
        user.setUsername("송호진");
        user.setEmail("0918syj@gmail.com");
        user.setPassword("1234");
        userRepository.save(user);
        Long userId = user.getId();

        // When
        UserAddress createdUserAddress = orderService.createDeliveryInfo(name, address, userId);

        // Then
        assertNotNull(createdUserAddress);
        assertNotNull(createdUserAddress.getId());
        assertEquals(name, createdUserAddress.getName());
        assertEquals(address, createdUserAddress.getAddress());
        assertEquals(userId, createdUserAddress.getUser().getId());
    }



}
package elice.eliceauction.domain.order.service;

import elice.eliceauction.domain.auction.entity.*;
import elice.eliceauction.domain.auction.repository.OrderRepository;
import elice.eliceauction.domain.auction.repository.MemberAddressRepository;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAddressRepository memberAddressRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateOrder() throws Exception {
        // Given
        Product product = new Product();
        product.setTitle("명품시계");
        product.setBrief("조금 비쌈");
        product.setPrice(10000L);
        product.setWatchCount(1L);

        Member member = new Member();
        member.setUsername("songhojin"); // 아이디 설정
        member.updatePassword(passwordEncoder, "1234"); // 비밀번호 설정
        member.updateName("송호진");

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setId(1L);
        memberAddress.setName("집");
        memberAddress.setAddress("광주광역시");

        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(1L);
        orderDto.setMemberId(1L);
        orderDto.setMemberAddressId(1L);

        // When
        Order createdOrder = orderService.createOrder(orderDto);

        // Then
        assertEquals(product, createdOrder.getProduct());
        assertEquals(member, createdOrder.getMember());
        assertEquals(memberAddress, createdOrder.getMemberAddress());
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

        Member member = new Member();
        member.setMembername("송호진");
        member.setEmail("0918syj@gmail.com");
        member.setPassword("1234");
        memberRepository.save(member);

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setName("집");
        memberAddress.setAddress("광주광역시");
        memberAddress.setMember(member);
        memberAddressRepository.save(memberAddress);

        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(product.getId());
        orderDto.setMemberId(member.getId());
        orderDto.setMemberAddressId(memberAddress.getId());

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

        Member member = new Member();
        member.setMembername("송호진");
        member.setEmail("0918syj@gmail.com");
        member.setPassword("1234");
        memberRepository.save(member);

        MemberAddress memberAddress1 = new MemberAddress();
        memberAddress1.setName("집");
        memberAddress1.setAddress("광주광역시");
        memberAddress1.setMember(member);
        memberAddressRepository.save(memberAddress1);

        MemberAddress memberAddress2 = new MemberAddress();
        memberAddress2.setName("회사");
        memberAddress2.setAddress("부산광역시");
        memberAddress2.setMember(member);
        memberAddressRepository.save(memberAddress2);

        // 주문 생성
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 및 시간 가져오기
        Order order = new Order(product, member, memberAddress1);
        order.setDate(now); // 주문 생성 시 현재 날짜 및 시간 설정
        orderRepository.save(order);

        // When
        // 주문 수정
        Order updatedOrder = orderService.updateOrder(new UpdateOrderDto());

        // Then
        assertNotNull(updatedOrder);
        // 수정된 주소 확인
        assertEquals(memberAddress2.getId(), updatedOrder.getMemberAddress().getId());
        // 주문 시간이 올바르게 설정되었는지 확인
        assertEquals(now, updatedOrder.getDate());
    }
    @Test
    public void testCreateDeliveryInfo() {
        // Given
        String name = "집";
        String address = "광주광역시";
        Member member = new Member();
        member.setMembername("송호진");
        member.setEmail("0918syj@gmail.com");
        member.setPassword("1234");
        memberRepository.save(member);
        Long memberId = member.getId();

        // When
        MemberAddress createdMemberAddress = orderService.createDeliveryInfo(new DeliveryDto() );

        // Then
        assertNotNull(createdMemberAddress);
        assertNotNull(createdMemberAddress.getId());
        assertEquals(name, createdMemberAddress.getName());
        assertEquals(address, createdMemberAddress.getAddress());
        assertEquals(memberId, createdMemberAddress.getMember().getId());
    }



}
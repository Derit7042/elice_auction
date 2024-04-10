package elice.eliceauction.domain.auction.service;

import elice.eliceauction.domain.auction.entity.*;
import elice.eliceauction.domain.auction.repository.OrderRepository;
import elice.eliceauction.domain.auction.repository.UserAddressRepository;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final UserAddressRepository userAddressRepository;

    // 모든 주문 조회
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 주문 생성
    public Order createOrder(OrderDto orderDto) {
        // 상품, 사용자 정보 가져오기
        Product product = productService.show(orderDto.getProductId());
        User user = userService.findUserById(orderDto.getUserId());

        // 사용자 주소 정보 가져오기
        UserAddress userAddress = userAddressRepository.getReferenceById(orderDto.getUserAddressId());

        // 주문 생성
        Order order = new Order(product, user, userAddress);

        // 주문 저장
        return orderRepository.save(order);
    }
    // 주문 취소
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    //주문 수정
    public Order updateOrder(UpdateOrderDto updateOrderDto) {
        // 주문 조회
        Order order = orderRepository.findById(updateOrderDto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. ID: " + updateOrderDto.getOrderId()));

        // 사용자가 주문한 배송지 정보 조회
        UserAddress userAddress = userAddressRepository.getReferenceById(updateOrderDto.getUserAddressId());

        // 주문한 상품의 배송지 정보 변경
        order.setUserAddress(userAddress);

        // 변경된 주문 정보 저장 후 반환
        return orderRepository.save(order);
    }

    // 배송 정보 생성
    public UserAddress createDeliveryInfo(DeliveryDto deliveryDto) {
        // 주문 배송 정보를 생성하기 위해 매개변수로 받은 정보를 사용하여 UserAddress 객체를 생성
        User user = userService.findUserById(deliveryDto.getUserId());

        UserAddress userAddress = new UserAddress();
        userAddress.setName(deliveryDto.getName());
        userAddress.setAddress(deliveryDto.getAddress()); // 주소 설정
        userAddress.setUser(user);

        return userAddressRepository.save(userAddress);
    }

    // 특정 사용자의 주문 목록 가져오기
    public List<Order> getOrdersByUser(Long userId) {
        User user = userService.findUserById(userId);
        return orderRepository.findByUser(user);
    }

    // 특정 상품에 대한 주문 목록 가져오기
    public List<Order> getOrdersByProduct(Long productId) {
        Product product = productService.show(productId);
        return orderRepository.findByProduct(product);
    }

    // 주문 상태 수정
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. ID: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}


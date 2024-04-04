package elice.eliceauction.domain.auction.service;

import elice.eliceauction.domain.auction.entity.Order;
import elice.eliceauction.domain.auction.entity.UserAddress;
import elice.eliceauction.domain.auction.repository.OrderRepository;
import elice.eliceauction.domain.auction.repository.UserAddressRepository;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        UserRepository userRepository, UserAddressRepository userAddressRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }


    // 주문 생성
    public Order createOrder(Long productId, Long userId, Long userAddressId, int price) {
        // 상품, 사용자 주문 정보를 기반으로 주문 생성 및 저장
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 사용자 주소 정보 가져오기
        UserAddress userAddress = userAddressRepository.findByUserId(userId);
        if (userAddress == null) {
            throw new EntityNotFoundException("사용자 주소 정보를 찾을 수 없습니다. 사용자 ID: " + userId);
        }

        Order order = new Order(product, user, price, userAddress);
        return orderRepository.save(order);
    }

    // 주문 저장
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    // 주문 취소
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    //주문 수정
    public Order updateOrder(Long orderId, Order.UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. ID: " + orderId));

        // 주문 정보 수정
        order.getUserAddress().setId(request.getUserAddressId());
        order.setPrice(request.getPrice());

        // 수정된 주문 저장 및 반환
        return orderRepository.save(order);
    }


    // 배송 정보 생성
    public UserAddress createDeliveryInfo(String name, Long userId) {
        // 주문 배송 정보를 생성하기 위해 매개변수로 받은 정보를 사용하여 UserAddress 객체를 생성
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        UserAddress userAddress = new UserAddress();
        userAddress.setName(name);
        userAddress.setUser(user);

        return userAddressRepository.save(userAddress);
    }

    // 특정 사용자의 주문 목록 가져오기
    public List<Order> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
        return orderRepository.findByUser(user);
    }

    // 특정 상품에 대한 주문 목록 가져오기
    public List<Order> getOrdersByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));
        return orderRepository.findByProduct(product);
    }

}
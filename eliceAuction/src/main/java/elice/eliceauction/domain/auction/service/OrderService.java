package elice.eliceauction.domain.auction.service;

import elice.eliceauction.domain.auction.entity.*;
import elice.eliceauction.domain.auction.repository.OrderRepository;
import elice.eliceauction.domain.auction.repository.MemberAddressRepository;
import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.service.MemberService;
import elice.eliceauction.exception.auction.InvalidOrderException;
import elice.eliceauction.exception.auction.OrderNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final CartService cartService;
    private final MemberAddressRepository memberAddressRepository;

    // 모든 주문 조회
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 주문 생성
    public Order createOrder(OrderDto orderDto) throws Exception {
        // 상품, 사용자 정보 가져오기
        Product product = productService.show(orderDto.getProductId());
        Member member = memberService.findMemberById(orderDto.getMemberId());

        // 사용자 주소 정보 가져오기
        MemberAddress memberAddress = memberAddressRepository.getReferenceById(orderDto.getMemberAddressId());

        // 주문 생성
        Order order = new Order(product, member, memberAddress);

        // 주문 저장
        return orderRepository.save(order);
    }
    public Order createLoginOrder(Member member, Product product, Long memberAddressId) {
        MemberAddress memberAddress = memberAddressRepository.findById(memberAddressId)
                .orElseThrow(() -> new EntityNotFoundException("주소를 찾을 수 없습니다."));

        Order newOrder = new Order(product, member, memberAddress);
        return orderRepository.save(newOrder);
    }
    public List<Order> createOrdersFromCart(Member member) {
        List<CartItem> cartItems = cartService.getCarts(member);

        if (cartItems.isEmpty()) {
            throw new InvalidOrderException("장바구니가 비어 있습니다.");
        }

        List<Order> orders = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            MemberAddress memberAddress = memberAddressRepository.findById(member.getId())
                    .orElseThrow(() -> new EntityNotFoundException("배송지 정보를 찾을 수 없습니다."));

            Product product = cartItem.getProduct();
            Order order = new Order(product, member, memberAddress);
            orders.add(orderRepository.save(order));
        }

        cartService.clear(member);
        return orders;
    }
    // 주문 취소
    public void cancelOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("주문을 찾을 수 없습니다. ID: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }


    //주문 수정
    public Order updateOrder(UpdateOrderDto updateOrderDto) {
        // 주문 조회
        Order order = orderRepository.findById(updateOrderDto.getOrderId())
                .orElseThrow(() -> new InvalidOrderException("주문을 찾을 수 없습니다. ID: " + updateOrderDto.getOrderId()));

        // 사용자가 주문한 배송지 정보 조회
        MemberAddress memberAddress = memberAddressRepository.getReferenceById(updateOrderDto.getMemberAddressId());

        // 주문한 상품의 배송지 정보 변경
        order.setMemberAddress(memberAddress);

        // 변경된 주문 정보 저장 후 반환
        return orderRepository.save(order);
    }

    // 배송 정보 생성
    public MemberAddress createDeliveryInfo(DeliveryDto deliveryDto) throws Exception {
        // 주문 배송 정보를 생성하기 위해 매개변수로 받은 정보를 사용하여 MemberAddress 객체를 생성
        Member member = memberService.findMemberById(deliveryDto.getMemberId());

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setName(deliveryDto.getName());
        memberAddress.setAddress(deliveryDto.getAddress()); // 주소 설정
        memberAddress.setMember(member);

        return memberAddressRepository.save(memberAddress);
    }

    // 특정 사용자의 주문 목록 가져오기
    public List<Order> getOrdersByMember(Long memberId) throws Exception {
        Member member = memberService.findMemberById(memberId);
        return orderRepository.findByMember(member);
    }

    // 특정 상품에 대한 주문 목록 가져오기
    public List<Order> getOrdersByProduct(Long productId) {
        Product product = productService.show(productId);
        return orderRepository.findByProduct(product);
    }

    // 주문 상태 수정
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidOrderException("주문을 찾을 수 없습니다. ID: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}


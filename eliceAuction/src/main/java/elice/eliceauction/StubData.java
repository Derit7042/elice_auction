package elice.eliceauction;

import elice.eliceauction.domain.auction.entity.DeliveryDto;
import elice.eliceauction.domain.auction.entity.OrderDto;
import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.member.dto.MemberSignUpDto;
import elice.eliceauction.domain.product.dto.ProductDto;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 테스트용 스텁데이터
 */
@Component
public class StubData implements CommandLineRunner {
    private final MemberService memberService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public StubData(MemberService memberService, OrderService orderService, ProductService productService, CartService cartService) {
        this.memberService = memberService;
        this.orderService = orderService;
        this.productService = productService;
        this.cartService = cartService;
    }



    public void MemberStubData() throws Exception{// 1~10번 회원 생성

        MemberSignUpDto dto;
        for(long i=1; i<=10; i++){
            dto = new MemberSignUpDto("username"+i, "password"+i, "name"+i);
            memberService.signUp(dto);
        }
    }

    public void ProductStubData(){// 상품 정보 생성
        ProductDto dto = new ProductDto();
        for(long i=1; i<=10; i++){
            dto.setTitle("title"+i);
            dto.setBrief("brief"+i);
            dto.setPrice(i*100);
            dto.setWatchCount(i);
            dto.setDate(LocalDateTime.now());
            dto.setPictureLink("https://isaiah0214.synology.me/gallery_colorize/fuck.jpg");
            productService.create(dto);
        }
    }

    public void OrderStubData() throws Exception {
        DeliveryDto deliveryDto = new DeliveryDto();

        for(long i=1; i<=10; i++){// 주소 정보 생성
            deliveryDto.setName("Name" + i);
            deliveryDto.setAddress("Address" + i);
            deliveryDto.setMemberId(i);
            orderService.createDeliveryInfo(deliveryDto);
        }

        OrderDto dto = new OrderDto();

        for(long i=1; i<=10; i++){// 주문 정보 생성
            dto.setMemberId(i);
            dto.setProductId(i);
            dto.setMemberAddressId(i);
            orderService.createOrder(dto);
        }
    }

    public void CartStubData() throws Exception {// 장바구니 정보 생성
        for(long i=1; i<=10; i++){
            Member targetMember = memberService.findMemberById(i);
            cartService.createCart(targetMember);// 장바구니 생성
            cartService.add(targetMember, i);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        MemberStubData();
        ProductStubData();
        OrderStubData();
        CartStubData();
    }
}

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
        // 1~10번 일반 회원 생성
        MemberSignUpDto dto;
        for(long i=1; i<=5; i++){
            dto = new MemberSignUpDto("username"+i, "!password"+i, "name"+i);
            memberService.signUp(dto);
        }

        // ADMIN 권한 회원 생성
        // 'admin'이라는 사용자명은 시스템에서만 유일하게 사용될 것을 가정함
        MemberSignUpDto adminDto = new MemberSignUpDto("admin", "!admin", "Administrator");
        memberService.signUpAdmin(adminDto);  // 별도의 관리자 등록 메서드를 호출
    }

    public void ProductStubData(){// 상품 정보 생성
        ProductDto dto = new ProductDto();
//        for(long i=1; i<=11; i++){
//            dto.setTitle("title"+i);
//            dto.setBrief("brief"+i);
//            dto.setPrice(i*100);
//            dto.setWatchCount(i);
//            dto.setDate(LocalDateTime.now());
//            dto.setPictureLink("https://isaiah0214.synology.me/gallery_colorize/fuck.jpg");
//            productService.create(dto);
//        }

        dto.setTitle("Life is rose");
        dto.setBrief("삶은 장미와도 같다는 것을 표현한 작품입니다.");
        dto.setPrice(7800000L);
        dto.setWatchCount(317L);
        dto.setDate(LocalDateTime.now());
        dto.setPictureLink("https://i.ibb.co/LdwFsGW/Kakao-Talk-20240419-222714857-02.png");
        productService.create(dto);

        dto.setTitle("빈 배");
        dto.setBrief("강에 떠있는 빈 배를 그린 작품입니다.");
        dto.setPrice(5500000L);
        dto.setWatchCount(193L);
        dto.setDate(LocalDateTime.now());
        dto.setPictureLink("https://i.ibb.co/dJDcb9p/Kakao-Talk-20240419-222714857-03.png");
        productService.create(dto);

        dto.setTitle("살아감");
        dto.setBrief("아무것도 없는 바다 위에 한 사람이 배를 타고 있지만 그래도 살아가고 있다는 것을 표현한 작품입니다.");
        dto.setPrice(19000000L);
        dto.setWatchCount(1834L);
        dto.setDate(LocalDateTime.now());
        dto.setPictureLink("https://i.ibb.co/g6mTFMW/Kakao-Talk-20240419-222714857-04.png");
        productService.create(dto);

        dto.setTitle("죽은 장미");
        dto.setBrief("죽은 장미를 그린 작품입니다.");
        dto.setPrice(6100000L);
        dto.setWatchCount(871L);
        dto.setDate(LocalDateTime.now());
        dto.setPictureLink("https://i.ibb.co/qBXzvxT/Kakao-Talk-20240419-222714857-01.png");
        productService.create(dto);

        dto.setTitle("권태를 부르는 성공");
        dto.setBrief("성공이 권태를 부른다는 것을 표현한 작품입니다.");
        dto.setPrice(3800000L);
        dto.setWatchCount(478L);
        dto.setDate(LocalDateTime.now());
        dto.setPictureLink("https://i.ibb.co/0Bz6cbh/Kakao-Talk-20240419-222714857.png");
        productService.create(dto);
    }

    public void OrderStubData() throws Exception {
        DeliveryDto deliveryDto = new DeliveryDto();

        for(long i=1; i<=5; i++){// 주소 정보 생성
            deliveryDto.setName("Name" + i);
            deliveryDto.setAddress("Address" + i);
            deliveryDto.setMemberId(i);
            orderService.createDeliveryInfo(deliveryDto);
        }

        OrderDto dto = new OrderDto();

        for(long i=1; i<=5; i++){// 주문 정보 생성
            dto.setMemberId(i);
            dto.setProductId(i);
            dto.setMemberAddressId(i);
            orderService.createOrder(dto);
        }
    }

    public void CartStubData() throws Exception {// 장바구니 정보 생성
        for(long i=1; i<=5; i++){
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

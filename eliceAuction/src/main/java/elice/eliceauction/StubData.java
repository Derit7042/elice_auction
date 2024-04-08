package elice.eliceauction;

import elice.eliceauction.domain.auction.service.OrderService;
import elice.eliceauction.domain.cart.service.CartService;
import elice.eliceauction.domain.product.dto.ProductDto;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.user.dto.UserRegistrationDto;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.entity.UserGrade;
import elice.eliceauction.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 테스트용 스텁데이터
 */
@Component
public class StubData implements CommandLineRunner {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public StubData(UserService userService, OrderService orderService, ProductService productService, CartService cartService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.cartService = cartService;
    }



    public void UserStubData(){// 1~10번 회원 생성
        UserRegistrationDto dto = new UserRegistrationDto();
        for(long i=1; i<=10; i++){
            dto.setUsername("username"+i);
            dto.setPassword("password"+i);
            dto.setEmail("email@"+i);
            userService.signUp(dto);
        }
        userService.changeUserGrade(1L, UserGrade.ADMIN.name());// 1번 회원은 ADMIN
    }

    public void ProductStubData(){// 상품 정보 생성
        ProductDto dto = new ProductDto();
        for(long i=1; i<=10; i++){
            dto.setTitle("title"+i);
            dto.setBrief("brief"+i);
            dto.setPrice(i*100);
            dto.setWatchCount(i);
            dto.setDate(LocalDateTime.now());
            dto.setPicture_link("https://isaiah0214.synology.me/gallery_colorize/fuck.jpg");
            productService.create(dto);
        }
    }

    public void OrderStubData(){
        
        for(long i=1; i<=10; i++){// 주소 정보 생성
            orderService.createDeliveryInfo("name"+i, "address"+i, i);
        }
        
        for(long i=1; i<=10; i++){// 주문 정보 생성
            orderService.createOrder(i, i, i, (int)i*100);
        }
    }

    public void CartStubData(){// 장바구니 정보 생성
        for(long i=1; i<=10; i++){
            User targetUser = userService.findUser(i);
            cartService.createCart(targetUser);// 장바구니 생성
            cartService.add(targetUser, i);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        UserStubData();
        ProductStubData();
        OrderStubData();
        CartStubData();
    }
}

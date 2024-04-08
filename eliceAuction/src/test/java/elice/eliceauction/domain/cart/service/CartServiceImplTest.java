package elice.eliceauction.domain.cart.service;

import elice.eliceauction.domain.cart.entity.Cart;
import elice.eliceauction.domain.cart.entity.CartItem;
import elice.eliceauction.domain.cart.repository.CartItemRepository;
import elice.eliceauction.domain.cart.repository.CartRepository;
import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.product.repository.ProductRepository;
import elice.eliceauction.domain.product.service.ProductService;
import elice.eliceauction.domain.user.entity.User;
import elice.eliceauction.domain.user.entity.UserGrade;
import elice.eliceauction.domain.user.repository.UserRepository;
import elice.eliceauction.exception.cart.InvalidCartException;
import elice.eliceauction.exception.cart.InvalidCartItemException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceImplTest {

    @Autowired
    CartService cartService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    
    /*
    * 이미 DB에 스텁데이터를 넣어둬서 안씀*/
//    @BeforeEach
//    void setUp() throws Exception {
//        for(long i=1; i<=10; i++){
//            User user = new User();
//            user.setUsername("username"+i);
//            user.setGrade(UserGrade.REGULAR);
//            user.setPassword("password"+i);
//            user.setEmail("email@email"+i);
//            user.setAddress("address"+i);
//            userRepository.save(user);
//
//            Product product = new Product();
//            product.setTitle("title"+i);
//            product.setBrief("brief"+i);
//            product.setPrice(i);
//            product.setWatchCount(i);
//            product.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
//            productRepository.save(product);
//        }
//        for(long i=1; i<=10; i++){
//            cartService.createCart(userRepository.findById(i).get());
//        }
//    }

    @Test
    @Transactional
    void getCarts() {
        // given
        List<CartItem> expected = new ArrayList<>();
        for(long i=1; i<=10; i++){
            CartItem cartItem = new CartItem();
            Cart cart = cartRepository.findByUser(userRepository.findById(1L).orElseThrow(() -> new RuntimeException("사용자 X"))).orElseThrow(()-> new InvalidCartException());
            cartItem.setCart(cart);
            cartItem.setProduct(productRepository.findById(i).get());

            cartItemRepository.save(cartItem);
            expected.add(cartItem);
        }

        // when

        // then
        assertEquals(expected, cartService.getCarts(userRepository.findById(1L).get()));
    }

    @Test
    void getCart() {
        // given
        List<CartItem> expected = new ArrayList<>();
        for(long i=1; i<=10; i++){

            Cart cart = cartRepository.findByUser(userRepository.findById(i).get()).get();
            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(productRepository.findById(i).get());

            cartItemRepository.save(cartItem);
            expected.add(cartItem);
        }

        // when

        // then
        for(long i=1; i<=10; i++){
            if(userRepository.findById(i).isPresent()){
                assertEquals(expected.get((int)i-1), cartService.getCart(userRepository.findById(i).get(), i) );
            }else{
                System.out.println("empty!");
            }

        }

    }

    @Test
    void isEmpty() {
        // given
        for(long i=1; i<=5; i++){// 1부터 5까지만 장바구니에 상품 넣음

            Cart cart = cartRepository.findByUser(userRepository.findById(i).get()).get();
            cart.setCount(cart.getCount() + 1);
            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(productRepository.findById(i).get());

            cartItemRepository.save(cartItem);
        }

        // when

        // then
        for(long i=1; i<=5; i++){
            assertEquals(false, cartService.isEmpty(userRepository.findById(i).get()) );
        }

        for(long i=6; i<=10; i++){
            assertEquals(true, cartService.isEmpty(userRepository.findById(i).get()) );
        }

    }

    @Test
    void clear() {
        // given
        for(long i=1; i<=5; i++){// id=1인 회원에게 상품 5개 입력

            Cart cart = cartRepository.findByUser(userRepository.findById(1L).get()).get();
            cart.setCount(cart.getCount() + 1);
            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(productRepository.findById(i).get());

            cartItemRepository.save(cartItem);
        }

        // when
        cartService.clear(userRepository.findById(1L).get());

        // then
        Cart cart = cartRepository.findByUser(userRepository.findById(1L).get()).get();
        assertTrue(cartItemRepository.findAllByCart(cart).isEmpty());
        assertTrue(cartService.isEmpty(userRepository.findById(1L).get()));

    }

    @Test
    void add() {
        // given
        // when
        for(long i=1; i<=5; i++){// id=1인 회원에게 상품 5개 입력
            cartService.add(userRepository.findById(1L).get(), i);

        }

        // then
        User user = userRepository.findById(1L).get();
        Cart cart = cartRepository.findByUser(user).get();
        assertEquals(5, cart.getCount() );
    }

    @Test
    void delete() {
        // given
        User targetUser = userRepository.findById(1L).get();
        for(long i=1; i<=5; i++){// id=1인 회원에게 상품 5개 입력

            Cart cart = cartRepository.findByUser(targetUser).get();
            cart.setCount(cart.getCount() + 1);
            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(productRepository.findById(i).get());

            cartItemRepository.save(cartItem);
        }
        // when
        /*
        * ISSEU #3: 테스트코드를 실행시키면
        * java.lang.ClassCastException: class java.lang.Integer cannot be cast to class elice.eliceauction.domain.cart.entity.CartItem
        * (java.lang.Integer is in module java.base of loader 'bootstrap'; elice.eliceauction.domain.cart.entity.CartItem is in unnamed module of loader 'app')
        * 의 오류가 발생하는데 왜 그런지 모르겠음..
        * */
        CartItem deleted = cartService.delete(targetUser, 1L);
        cartService.delete(targetUser, 2L);

        assertEquals(3, cartRepository.findByUser(targetUser)
                                        .get().getCount());// 상품 갯수 확인

        assertThrows(InvalidCartItemException.class, ()-> cartService.delete(targetUser, 1L) );// 중복삭제 예외
        assertThrows(RuntimeException.class, ()-> cartService.delete(targetUser, 2L) );// 중복삭제 예외

    }

    @Test
    void getCount() {
        // given
        User targetUser = userRepository.findById(1L).get();
        for(long i=1; i<=5; i++){// id=1인 회원에게 상품 5개 입력
//            Cart cart = cartRepository.findByUser(targetUser).get();
//            cart.setCount(cart.getCount() + 1);
//            CartItem cartItem = new CartItem();
//
//            cartItem.setCart(cart);
//            cartItem.setProduct(productRepository.findById(i).get());
//
//            cartItemRepository.save(cartItem);
            cartService.add(targetUser, i);
        }

        // when

        //then
        assertEquals(5, cartService.getCount(targetUser));
        cartService.delete(targetUser, 4L);
        assertEquals(4, cartService.getCount(targetUser));
    }
}
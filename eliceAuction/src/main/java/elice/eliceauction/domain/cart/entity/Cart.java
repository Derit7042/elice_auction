package elice.eliceauction.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ColumnDefault("0")
    private int cnt;

    // TODO: User 엔티티 개발시 연결
//    @OneToOne
//    @JoinColumn(name = "user_id")// 1:1 단방향
//    User user;

    /*TODO:
     * Product 엔티티 개발시 연결,
     * 연관관계 매핑
     * ISSUE #1:
     * CONDITION:
     * 1. 유저는 고유한 하나의 장바구니를 갖는다.
     * 2. 유저는 장바구니에 여러 상품을 담을 수 있다.
     * 3. 여러 유저가 하나의 상품을 장바구니에 동시에 담을 수 있다.
     * PROBLEM:
     * 이런 상황에서는 장바구니 - 상품의 연관관계가 N:M(다대 다) 단방향(M -> N) 연관관계가 되야 하는가?
     * 또한, 연관관계의 방향은 양방향인가? 아니면 단방향인가?
     * -------------------------------------------------------------
     * 또한, 단방향 매핑과 양방향 매핑의 정확한 차이와 그 의미는 무엇인가?
     * EX)
     * ~~한 상황에서는 단방향 매핑을 추천한다. -> 양방향 매핑이 안되는 이유 혹은 추천하지 않는 이유
     * */
//    List<Product> products;

}

package elice.eliceauction.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id 값, Cart 테이블과 Order 테이블에서 참조함

    @Column(name = "title", nullable = false) // 상품의 제목
    private String title;

    @Column(name = "brief", nullable = false) // 등록한 상품에 대한 간단한 설명
    private String brief;

    @Column(name = "price", nullable = false) // product 의 가격
    private Long price;

//    @Column(name = "picture_link", nullable = false) // 그림 링크
//    private String pictureLink;

//    @Column(name = "watch_Cnt", columnDefinition = "integer default 0", nullable = false) // 상품 조회수
//    private Long watchCount;

//    @OneToOne
//    @JoinColumn(name = "seller_id", nullable = false) // User 테이블의 id값 참조 (판매자)
//    private User seller;

//    @OneToOne
//    @JoinColumn(name = "buyer_id", nullable = false) // User 테이블의 id값 참조 (구매자)
//    private User buyer;


//    @OneToOne
//    @Column(name = "user_address_id", nullable = false) // User_address 테이블의 id 값 참조
//    private UserAddress userAddress;

//    @Column(name = "date", nullable = false) // 상품이 등록된 날짜 및 시간
//    private LocalDateTime date;


//    @Column(name = "date", nullable = false, updatable = false)
//    public LocalDateTime Date() {
//        this.date = LocalDateTime.now();
//        return date;
//    }

    public void patch(Product product) {
        if (product.title != null)
            this.title = product.title;
        if (product.brief != null)
            this.brief = product.brief;
        if (product.price != null)
            this.price = product.price;
    }

}

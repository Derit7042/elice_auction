package elice.eliceauction.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
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

    @Column(name = "watch_Cnt", nullable = false) // 상품 조회수
    private Long watchCount;

    @Column(name = "date") // 상품이 등록된 날짜 및 시간
    private LocalDateTime date;

    @Column(name = "picture_link") // 그림 링크
    private String pictureLink;

//    @Column(name = "seller_id")
//    private Long sellerId;

//    @Column(name = "seller_name")
//    private String sellerName;

    public Product(String title, String brief, Long price, Long watchCount, LocalDateTime date, String pictureLink) {
        this.title = title;
        this.brief = brief;
        this.price = price;
        this.watchCount = watchCount;
        this.date = date;
        this.pictureLink = pictureLink;
    }

//    @OneToOne
//    @JoinColumn(name = "seller_id", nullable = false) // Member 테이블의 id값 참조 (판매자)
//    private Member seller;

//    @OneToOne
//    @JoinColumn(name = "buyer_id", nullable = false) // Member 테이블의 id값 참조 (구매자)
//    private Member buyer;

    public void patch(Product product) {
        if (product.title != null)
            this.title = product.title;
        if (product.brief != null)
            this.brief = product.brief;
        if (product.price != null)
            this.price = product.price;
    }
}

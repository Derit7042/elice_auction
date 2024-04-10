package elice.eliceauction.domain.auction.entity;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.member.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "`Order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_address_id") // 사용자 주소와의 관계를 표시하는 외래 키
    private UserAddress userAddress;


    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    //배송 상태
    @Column(name = "status")
    private String status;

    public Order(Product product, User user, UserAddress userAddress) {
        this.product = product;
        this.user = user;
        this.userAddress = userAddress;
        this.date = LocalDateTime.now(); // 주문 생성 시 현재 시간 설정
    }

}

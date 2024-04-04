package elice.eliceauction.domain.auction.entity;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.user.entity.User;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery")
    private UserAddress userAddress;

    @Column(name = "price")
    private int price;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    public Order(Product product, User user, int price, UserAddress userAddress) {
        this.product = product;
        this.user = user;
        this.price = price;
        this.userAddress = userAddress;
        this.date = LocalDateTime.now(); // 주문 생성 시 현재 시간 설정
    }
    @Getter
    @Setter
    public static class UpdateOrderRequest {
        private Long userAddressId;
        private int price;
    }
}

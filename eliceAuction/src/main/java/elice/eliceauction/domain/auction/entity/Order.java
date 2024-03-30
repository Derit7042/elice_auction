package elice.eliceauction.domain.auction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "price")
    private int price;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "address", nullable = false)
    private String address;

    public void Order(Product product, User user, int price, String address){
        this.product = product;
        this.user = user;
        this.price = price;
        this.address = address;
    }
    public void Date(){
        this.date = LocalDateTime.now();
    }
}

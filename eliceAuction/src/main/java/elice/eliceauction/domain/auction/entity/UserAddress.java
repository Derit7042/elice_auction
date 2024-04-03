package elice.eliceauction.domain.auction.entity;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address", nullable = false)
    private String address;

    public void UserAddress(String name, User user, String address){
        this.name = name;
        this.user = user;
        this.address = address;
    }
    @OneToOne(mappedBy = "user_address_id")
    private Product product;
}

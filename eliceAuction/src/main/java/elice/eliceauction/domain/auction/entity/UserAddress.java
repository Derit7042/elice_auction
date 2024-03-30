package elice.eliceauction.domain.auction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rn_address", nullable = false)
    private String rn_address;

    public void UserAddress(String name, User user, String rn_address){
        this.name = name;
        this.user = user;
        this.rn_address = rn_address;
    }
    @OneToOne(mappedBy = "user_address_id")
    private Product product;
}

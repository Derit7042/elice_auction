package elice.eliceauction.domain.cart.entity;

import elice.eliceauction.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToOne
    @JoinColumn(name = "user_id")// 1:1 단방향
     User user;


    @OneToMany(mappedBy = "cart")
    List<CartItem> cartItems = new ArrayList<>();

}

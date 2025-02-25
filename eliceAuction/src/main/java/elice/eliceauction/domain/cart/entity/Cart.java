package elice.eliceauction.domain.cart.entity;

import elice.eliceauction.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ColumnDefault("0")
    private int count;

    @OneToOne
    @JoinColumn(name = "member_id")// 1:1 단방향
    Member member;

}

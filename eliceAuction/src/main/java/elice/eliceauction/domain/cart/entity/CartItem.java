package elice.eliceauction.domain.cart.entity;

import elice.eliceauction.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public static CartResponseDto toDto(CartItem item){
        Product product = item.getProduct();

        return CartResponseDto.builder()
                .title(product.getTitle())
                .price(product.getPrice())
                .pictureLink(product.getPictureLink())
                .build();
    }
}

package elice.eliceauction.domain.product.dto;

import elice.eliceauction.domain.product.entity.Product;
import lombok.Setter;

@Setter
public class ProductDto {
    private Long id;
    private String title;
    private String brief;
    private Long price;
    private Long watchCount;
    //    private String picture_link;


    public Product toEntity() {
        return new Product(id, title, brief, price, watchCount);
    }
}

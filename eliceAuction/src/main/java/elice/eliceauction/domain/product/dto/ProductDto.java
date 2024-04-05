package elice.eliceauction.domain.product.dto;

import elice.eliceauction.domain.product.entity.Product;

public class ProductDto {
    private Long id;
    private String title;
    private String brief;
    //    private String picture_link;
    //    private Long watchCount;
    private Long price;


    public Product toEntity() {
        return new Product(id, title, brief, price);
    }
}

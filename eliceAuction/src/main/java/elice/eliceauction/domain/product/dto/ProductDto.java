package elice.eliceauction.domain.product.dto;

import elice.eliceauction.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
@Slf4j
public class ProductDto {
    private Long id;
    private String title;
    private String brief;
    private Long price;
    private Long watchCount;
    private LocalDateTime date;
    //    private String picture_link;

    public Product toEntity() {
        return new Product(id, title, brief, price, watchCount, date);
    }

}

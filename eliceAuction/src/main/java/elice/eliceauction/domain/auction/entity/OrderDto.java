package elice.eliceauction.domain.auction.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long productId;
    private Long userId;
    private Long userAddressId;
    private int price;
}

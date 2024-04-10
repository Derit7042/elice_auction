package elice.eliceauction.domain.auction.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class OrderDto {
    private Long productId;
    private Long memberId;
    private Long memberAddressId;

    public OrderDto () {
    }
}

package elice.eliceauction.domain.auction.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDto {
    private Long orderId;
    private Long memberAddressId;
    private Long status;

    public UpdateOrderDto() {

    }
}

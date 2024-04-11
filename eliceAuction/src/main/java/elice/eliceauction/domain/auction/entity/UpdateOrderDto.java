package elice.eliceauction.domain.auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDto {
    private Long orderId;
    private Long memberAddressId;
    private String status;

    public UpdateOrderDto() {

    }
}

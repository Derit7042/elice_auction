package elice.eliceauction.domain.auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDto {
    private Long orderId;
    private Long memberAddressId;
    @JsonProperty
    private String status;

}

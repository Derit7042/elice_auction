package elice.eliceauction.domain.auction.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDto {
    private String name;
    private String address;
    private Long memberId;
}


package elice.eliceauction.domain.auction.entity

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter

@Schema(description = "주문 응답 DTO")
@Getter
@Builder
class OrderIdDto {
    @Schema(description = "작품명")
    String title;
    

}
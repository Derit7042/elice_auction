package elice.eliceauction.domain.cart.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "장바구니 응답 DTO")// 스웨거 어노테이션
@Getter
@Builder
public class CartResponseDto {
    @Schema(description = "작품명")
    String title;
    @Schema(description = "작품 가격")
    Long price;
    @Schema(description = "작품 사진")
    String pictureLink;
}

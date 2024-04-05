package elice.eliceauction.domain.auction.entity;

import elice.eliceauction.domain.product.entity.Product;
import elice.eliceauction.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long productId;
    private Long userId;
    private Long userAddressId;
    private int price;

    public OrderDto(Product product, User user, UserAddress userAddress, Order order) {
        this.productId = product.getId();
        this.userId = user.getId();
        this.userAddressId = userAddress.getId();
        this.price = order.getPrice();
    }
}

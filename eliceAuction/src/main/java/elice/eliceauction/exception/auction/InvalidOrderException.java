package elice.eliceauction.exception.auction;

import elice.eliceauction.domain.auction.entity.UpdateOrderDto;
import elice.eliceauction.exception.RootException;


public class InvalidOrderException extends RootException {
    public InvalidOrderException(){
        super("주문을 찾을 수 없습니다");
    }
    public InvalidOrderException(String message) {
        super(message);
    }
}

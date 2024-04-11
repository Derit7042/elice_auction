package elice.eliceauction.exception.auction;

import elice.eliceauction.exception.RootException;

public class OrderNotFoundException extends RootException {
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
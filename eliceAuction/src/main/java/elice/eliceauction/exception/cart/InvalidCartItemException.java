package elice.eliceauction.exception.cart;

import elice.eliceauction.exception.RootException;

public class InvalidCartItemException extends RootException {
    public InvalidCartItemException() {
        super("카트에 해당 상품이 존재하지 않습니다.");
    }

    public InvalidCartItemException(String message) {
        super(message);
    }
}

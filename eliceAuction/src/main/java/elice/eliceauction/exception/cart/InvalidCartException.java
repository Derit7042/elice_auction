package elice.eliceauction.exception.cart;

import elice.eliceauction.exception.RootException;

public class InvalidCartException extends RootException {
    public InvalidCartException() {
        super("해당 유저의 카드가 존재하지 않습니다.");
    }

    public InvalidCartException(String message) {
        super(message);
    }
}

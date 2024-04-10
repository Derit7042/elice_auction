package elice.eliceauction.exception.cart;

import elice.eliceauction.exception.RootException;

public class DuplicatedCartItemException extends RootException {
    public DuplicatedCartItemException() {
        super("해당 상품이 이미 장바구니에 존재합니다..");
    }

    public DuplicatedCartItemException(String message) {
        super(message);
    }
}

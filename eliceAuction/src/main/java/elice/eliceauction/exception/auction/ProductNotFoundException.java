package elice.eliceauction.exception.auction;

import elice.eliceauction.exception.RootException;

public class ProductNotFoundException extends RootException {
    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}

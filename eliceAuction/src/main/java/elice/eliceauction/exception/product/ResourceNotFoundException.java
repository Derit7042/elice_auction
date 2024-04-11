package elice.eliceauction.exception.product;

import elice.eliceauction.exception.RootException;

public class ResourceNotFoundException extends RootException {

    public ResourceNotFoundException() {
        super("해당 상품은 존재하지 않습니다.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
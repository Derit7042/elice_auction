package elice.eliceauction.exception.auction;

import elice.eliceauction.exception.RootException;

public class MemberNotFoundException extends RootException {
    public MemberNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}

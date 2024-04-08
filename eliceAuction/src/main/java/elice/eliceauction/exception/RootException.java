package elice.eliceauction.exception;

import lombok.Getter;

@Getter
public class RootException extends RuntimeException {
    private boolean isSuccess;
    private String detailMessage;

    public RootException(String message) {
        this.isSuccess = false;
        this.detailMessage = message;
    }
}

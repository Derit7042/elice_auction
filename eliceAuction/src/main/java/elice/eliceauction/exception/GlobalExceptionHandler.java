package elice.eliceauction.exception;

import elice.eliceauction.exception.cart.DuplicatedCartItemException;
import elice.eliceauction.exception.cart.InvalidCartException;
import elice.eliceauction.exception.cart.InvalidCartItemException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedCartItemException.class)
    public ResponseEntity<String> handleDuplicatedCartItemException(DuplicatedCartItemException e) {
        ResponseEntity<String> body = ResponseEntity.badRequest().body(e.getMessage());
        return body;
    }

    @ExceptionHandler(InvalidCartException.class)
    public ResponseEntity<String> handleInvalidCartException(InvalidCartException e) {
        ResponseEntity<String> body = ResponseEntity.notFound().build();
        return body;
    }

    @ExceptionHandler(InvalidCartItemException.class)
    public ResponseEntity<String> handleInvalidCartItemException(InvalidCartItemException e) {
        ResponseEntity<String> body = ResponseEntity.badRequest().body(e.getMessage());
        return body;
    }
}

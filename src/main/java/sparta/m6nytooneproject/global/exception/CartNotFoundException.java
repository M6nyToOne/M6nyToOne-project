package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends ServiceException {
    public CartNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

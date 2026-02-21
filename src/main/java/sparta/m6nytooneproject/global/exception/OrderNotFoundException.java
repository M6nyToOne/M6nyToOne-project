package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ServiceException {
    public OrderNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

package sparta.m6nytooneproject.global.exception.order;

import org.springframework.http.HttpStatus;

public class CancelOrderException extends OrderException {
    public CancelOrderException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

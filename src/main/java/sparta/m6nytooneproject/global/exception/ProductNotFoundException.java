package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ServiceException {
    public ProductNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

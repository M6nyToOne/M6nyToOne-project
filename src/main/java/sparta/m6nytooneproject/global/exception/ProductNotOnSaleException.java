package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class ProductNotOnSaleException extends ServiceException {
    public ProductNotOnSaleException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

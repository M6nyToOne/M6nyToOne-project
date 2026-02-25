package sparta.m6nytooneproject.global.exception.product;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class ProductNotFoundException extends ServiceException {
    public ProductNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

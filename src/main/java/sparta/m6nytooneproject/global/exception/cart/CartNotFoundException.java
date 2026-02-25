package sparta.m6nytooneproject.global.exception.cart;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class CartNotFoundException extends ServiceException {
    public CartNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

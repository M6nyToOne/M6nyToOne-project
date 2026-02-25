package sparta.m6nytooneproject.global.exception.cart;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class OutOfStockException extends ServiceException {
    public OutOfStockException(String message) {

        super(HttpStatus.BAD_REQUEST, message);
    }
}

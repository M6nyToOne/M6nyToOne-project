package sparta.m6nytooneproject.global.exception.order;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class OrderException extends ServiceException {

    public OrderException(HttpStatus status, String message) {
        super(status, message);
    }
}

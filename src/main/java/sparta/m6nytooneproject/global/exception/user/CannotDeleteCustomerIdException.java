package sparta.m6nytooneproject.global.exception.user;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class CannotDeleteCustomerIdException extends ServiceException {
    public CannotDeleteCustomerIdException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

package sparta.m6nytooneproject.global.exception.user;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class AlreadyLoginUserException extends ServiceException {
    public AlreadyLoginUserException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

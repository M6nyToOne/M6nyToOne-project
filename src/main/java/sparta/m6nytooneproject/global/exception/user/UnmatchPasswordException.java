package sparta.m6nytooneproject.global.exception.user;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class UnmatchPasswordException extends ServiceException {
    public UnmatchPasswordException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

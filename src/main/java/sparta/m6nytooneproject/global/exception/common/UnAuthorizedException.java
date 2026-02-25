package sparta.m6nytooneproject.global.exception.common;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ServiceException {
    public UnAuthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

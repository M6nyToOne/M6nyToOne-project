package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class UnAuthurizedException extends ServiceException {
    public UnAuthurizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

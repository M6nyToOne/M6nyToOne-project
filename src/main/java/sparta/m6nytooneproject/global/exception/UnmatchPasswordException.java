package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class UnmatchPasswordException extends ServiceException {
    public UnmatchPasswordException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

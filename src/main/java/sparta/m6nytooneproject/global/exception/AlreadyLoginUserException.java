package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyLoginUserException extends ServiceException {
    public AlreadyLoginUserException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

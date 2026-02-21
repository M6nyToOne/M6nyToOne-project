package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistingEmailException extends ServiceException {
    public AlreadyExistingEmailException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

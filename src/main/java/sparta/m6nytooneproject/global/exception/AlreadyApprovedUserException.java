package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyApprovedUserException extends ServiceException {
    public AlreadyApprovedUserException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

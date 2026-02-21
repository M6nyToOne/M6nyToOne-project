package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistingReviewException extends ServiceException {
    public AlreadyExistingReviewException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

package sparta.m6nytooneproject.global.exception.review;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class AlreadyExistingReviewException extends ServiceException {
    public AlreadyExistingReviewException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

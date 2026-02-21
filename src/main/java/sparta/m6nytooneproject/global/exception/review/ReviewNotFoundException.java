package sparta.m6nytooneproject.global.exception.review;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class ReviewNotFoundException extends ServiceException {
    public ReviewNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

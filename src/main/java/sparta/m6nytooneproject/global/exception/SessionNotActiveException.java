package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class SessionNotActiveException extends ServiceException{
    public SessionNotActiveException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

package sparta.m6nytooneproject.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadySameRoleException extends ServiceException {
    public AlreadySameRoleException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

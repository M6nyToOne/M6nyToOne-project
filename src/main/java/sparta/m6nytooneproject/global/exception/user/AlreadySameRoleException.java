package sparta.m6nytooneproject.global.exception.user;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class AlreadySameRoleException extends ServiceException {
    public AlreadySameRoleException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

package sparta.m6nytooneproject.global.exception.order;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public abstract class OrderException extends ServiceException {
    public OrderException(HttpStatus status, String message) {
        super(status, "주문 처리중 에러 발생 : " + message);
    }
}

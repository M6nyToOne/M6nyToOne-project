package sparta.m6nytooneproject.global.exception.product;

import org.springframework.http.HttpStatus;
import sparta.m6nytooneproject.global.exception.common.ServiceException;

public class ProductNotOnSaleException extends ServiceException {
    public ProductNotOnSaleException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

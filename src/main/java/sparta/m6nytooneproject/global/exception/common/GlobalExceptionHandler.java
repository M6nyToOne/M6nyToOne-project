package sparta.m6nytooneproject.global.exception.common;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.exception.order.OrderException;

import javax.management.OperationsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleServiceException(ServiceException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleOrderException(OrderException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiResponseDto.error(ex.getMessage()));
    }
}

package sparta.m6nytooneproject.global.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleServiceException(ServiceException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiResponseDto.error(ex.getMessage()));
    }
}

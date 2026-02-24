package sparta.m6nytooneproject.global.exception.common;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponseDto<Void> handleRuntimeException(RuntimeException ex) {
        return ApiResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public ApiResponseDto<Void> handleServiceException(ServiceException ex) {
        return ApiResponseDto.error(ex.getStatus(), ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDto<Map<String, String>> dtoValidation(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ApiResponseDto.errorWithMap(HttpStatus.BAD_REQUEST, errors , "잘못된 요청입니다.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponseDto<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ApiResponseDto.error(HttpStatus.BAD_REQUEST , ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponseDto<Void> handleExpiredJwtException(ExpiredJwtException e) {
        return ApiResponseDto.error(HttpStatus.UNAUTHORIZED , e.getMessage());
    }
}

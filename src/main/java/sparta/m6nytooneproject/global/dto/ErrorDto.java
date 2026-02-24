package sparta.m6nytooneproject.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ErrorDto <T> implements ApiResponseDto<T>{
    private final HttpStatus status;
    private final String message;
    private final T data;
}


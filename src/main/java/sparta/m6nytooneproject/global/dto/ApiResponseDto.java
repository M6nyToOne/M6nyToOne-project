package sparta.m6nytooneproject.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiResponseDto<T> {
    String status;
    String message;
    T data;

    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>("Success!", null, data);
    }

    public static <T> ApiResponseDto<T> successWithNoContent() {
        return new ApiResponseDto<>("Success!", null, null);
    }

    public static <T> ApiResponseDto<T> error(String message) {
        return new ApiResponseDto<>("Error!", message, null);
    }

    public static <T> ApiResponseDto<T> errorWithMap(T map, String message) {
        return new ApiResponseDto<>("Error!", message, map);
    }
}

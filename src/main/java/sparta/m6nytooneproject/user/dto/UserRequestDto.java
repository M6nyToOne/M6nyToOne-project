package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.UserRole;

@Getter
public class UserRequestDto {

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;

    @NotNull(message = "필수 입력 항목입니다.")
    private UserRole userRole;
}

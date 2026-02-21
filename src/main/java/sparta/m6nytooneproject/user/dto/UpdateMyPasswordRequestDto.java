package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateMyPasswordRequestDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
    private String newPassword;
}

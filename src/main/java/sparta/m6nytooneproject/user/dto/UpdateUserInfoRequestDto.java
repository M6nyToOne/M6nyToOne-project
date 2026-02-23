package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserInfoRequestDto {

    @NotBlank(message = "변경사항을 입력해주세요.")
    private String userName;

    @NotBlank(message = "변경사항을 입력해주세요.")
    private String email;

    @NotBlank(message = "변경사항을 입력해주세요.")
    private String phoneNumber;
}

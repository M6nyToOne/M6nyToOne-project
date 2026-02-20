package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.LoginStatus;

@Getter
public class UpdateUserRequestDto {

    @NotBlank(message = "상태를 변경해주세요.")
    private LoginStatus loginStatus;
}

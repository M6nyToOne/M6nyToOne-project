package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRegisteredRequestDto {

    @NotNull(message = "변경할 역할을 입력해주세요.")
    private String userRole;
}

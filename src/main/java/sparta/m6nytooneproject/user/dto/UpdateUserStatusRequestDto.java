package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.SignupStatus;

@Getter
public class UpdateUserStatusRequestDto {

    @NotNull(message = "상태를 변경해주세요.")
    private SignupStatus signupStatus;

    private String rejectMessage;
}

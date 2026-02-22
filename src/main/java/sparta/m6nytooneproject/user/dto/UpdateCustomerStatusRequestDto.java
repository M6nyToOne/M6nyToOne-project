package sparta.m6nytooneproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sparta.m6nytooneproject.user.entity.SignupStatus;

@Getter
public class UpdateCustomerStatusRequestDto {

    @NotNull
    private SignupStatus signupStatus;
}

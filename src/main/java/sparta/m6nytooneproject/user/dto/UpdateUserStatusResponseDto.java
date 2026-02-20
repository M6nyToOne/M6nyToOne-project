package sparta.m6nytooneproject.user.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class UpdateUserStatusResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String phoneNumber;
    private final SignupStatus signupStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateUserStatusResponseDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.signupStatus = user.getSignupStatus();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}

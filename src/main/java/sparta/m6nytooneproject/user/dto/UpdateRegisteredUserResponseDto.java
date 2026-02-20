package sparta.m6nytooneproject.user.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;

import java.time.LocalDateTime;

@Getter
public class UpdateRegisteredUserResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String phoneNumber;
    private final UserRole userRole;
    private final SignupStatus signupStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateRegisteredUserResponseDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.userRole = user.getRole();
        this.signupStatus = user.getSignupStatus();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}

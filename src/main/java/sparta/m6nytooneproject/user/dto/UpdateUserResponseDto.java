package sparta.m6nytooneproject.user.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.LoginStatus;
import sparta.m6nytooneproject.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class UpdateUserResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String phoneNumber;
    private final LoginStatus loginStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateUserResponseDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.loginStatus = user.getLoginStatus();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}

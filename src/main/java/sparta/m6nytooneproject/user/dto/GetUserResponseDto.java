package sparta.m6nytooneproject.user.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;

import java.time.LocalDateTime;

@Getter
public class GetUserResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String phoneNumber;

    public GetUserResponseDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}

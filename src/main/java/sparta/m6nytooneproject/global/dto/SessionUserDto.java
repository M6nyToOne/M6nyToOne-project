package sparta.m6nytooneproject.global.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;

@Getter
public class SessionUserDto {
    private final Long id;
    private final String userName;
    private final String email;
    private final String password;
    private final SignupStatus signupStatus;
    private final UserRole userRole;

    public SessionUserDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.signupStatus = user.getSignupStatus();
        this.userRole = user.getRole();
    }
}

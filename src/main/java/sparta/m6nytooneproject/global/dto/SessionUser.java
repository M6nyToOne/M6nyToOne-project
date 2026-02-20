package sparta.m6nytooneproject.global.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.LoginStatus;
import sparta.m6nytooneproject.user.entity.User;

@Getter
public class SessionUser {
    private final Long id;
    private final String userName;
    private final String email;
    private final String password;
    private final LoginStatus loginStatus;

    public SessionUser(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.loginStatus = user.getLoginStatus();
    }
}

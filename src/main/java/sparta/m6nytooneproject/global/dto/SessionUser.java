package sparta.m6nytooneproject.global.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.User;

@Getter
public class SessionUser {
    private final Long id;
    private final String username;
    private final String email;
    private final String password;

    public SessionUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}

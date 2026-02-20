package sparta.m6nytooneproject.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sparta.m6nytooneproject.global.entity.BaseEntity;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // setter로 상태 변경...?
    @Setter
    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

    public User(String userName, String email, String password, String phoneNumber, UserRole userRole) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = userRole;
    }

    public void updateLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }
}

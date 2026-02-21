package sparta.m6nytooneproject.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import sparta.m6nytooneproject.config.PasswordEncoder;
import sparta.m6nytooneproject.global.entity.BaseEntity;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "deleted")
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
    private SignupStatus signupStatus;

    public User(String userName, String email, String password, String phoneNumber, UserRole userRole) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = userRole;
    }

    public void updateSignupStatus(SignupStatus signupStatus) {
        this.signupStatus = signupStatus;
    }

    public void updateUserRole(UserRole userRole) {
        this.role = userRole;
    }

    public void updateUserInfo(String userName, String email, String phoneNumber) {
        this.userName =userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }


}

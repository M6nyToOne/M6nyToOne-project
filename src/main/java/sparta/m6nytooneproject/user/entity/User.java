package sparta.m6nytooneproject.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import sparta.m6nytooneproject.global.entity.BaseEntity;

import java.time.LocalDateTime;

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

    // 승인일,거부일 추가
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;

    // 거부 사유
    private String rejectReason;

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

    // 상태변경 메서드
    public void updateSignupStatus(SignupStatus signupStatus) {
        this.signupStatus = signupStatus;
    }

    // 역할 변경 메서드
    public void updateUserRole(UserRole userRole) {
        this.role = userRole;
    }

    // 관리자 정보 수정 메서드
    public void updateUserInfo(String userName, String email, String phoneNumber) {
        if (userName != null && !userName.isBlank()) {
            this.userName =userName;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        }
    }

    // 관리자 비밀번호 변경 메서드
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    // 승인 메서드
    public void approve() {
        if (signupStatus != SignupStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태가 아닙니다.");
        }
        signupStatus = SignupStatus.ACTIVE;
        approvedAt = LocalDateTime.now();
    }

    // 거부 메서드
    public void reject(String reason) {
        if (signupStatus != SignupStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태가 아닙니다.");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalStateException("거부사유는 필수입니다.");
        }
        signupStatus = SignupStatus.REJECTED;
        rejectedAt = LocalDateTime.now();
        rejectReason = reason;
    }

    public static User ofToken(Long id, String email, UserRole role) {
        User user = new User();
        user.id = id;
        user.email = email;
        user.role = role;
        return user;
    }
}

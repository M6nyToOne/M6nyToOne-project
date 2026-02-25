package sparta.m6nytooneproject.user.dto;

import lombok.Getter;
import sparta.m6nytooneproject.order.entity.Order;
import sparta.m6nytooneproject.user.entity.SignupStatus;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.entity.UserRole;

import java.time.LocalDateTime;

@Getter
public class GetAllCustomerResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String phoneNumber;
    private final SignupStatus signupStatus;
    private final LocalDateTime createdAt;

    // Lv.1 고객 조회 데이터 확장
    private final Long orderCount;
    private final Long totalAmount;

    public GetAllCustomerResponseDto(User user, Long orderCount, Long totalAmount) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.signupStatus = user.getSignupStatus();
        this.createdAt = user.getCreatedAt();
        this.orderCount = orderCount;
        this.totalAmount = totalAmount;
    }
}

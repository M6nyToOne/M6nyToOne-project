package sparta.m6nytooneproject.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserInfoRequestDto {

    private String userName;
    private String email;
    private String phoneNumber;
}

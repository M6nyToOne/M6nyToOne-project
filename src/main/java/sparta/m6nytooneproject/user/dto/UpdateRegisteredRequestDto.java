package sparta.m6nytooneproject.user.dto;

import lombok.Getter;
import sparta.m6nytooneproject.user.entity.UserRole;

@Getter
public class UpdateRegisteredRequestDto {

    private UserRole userRole;
}

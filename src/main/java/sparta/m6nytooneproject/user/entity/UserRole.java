package sparta.m6nytooneproject.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    SUPER_ADMIN("ROLE_SUPER", "전체 관리자"),
    OPER_ADMIN("ROLE_OPER", "운영 관리자"),
    MARKET_ADMIN("ROLE_MARKET", "마케팅 관리자"),
    CS_ADMIN("ROLE_CS", "CS 관리자"),
    CUSTOMER("ROLE_CUSTOMER", "고객");

    private final String key;
    private final String title;
}

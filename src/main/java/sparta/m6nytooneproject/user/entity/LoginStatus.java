package sparta.m6nytooneproject.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginStatus {

    ACTIVE("STATUS_ACTIVE", "활성"),
    INACTIVE("STATUS_INACTIVE", "비활성"),
    SUSPEND("STATUS_SUSPEND", "정지"),
    PENDING("STATUS_PENDING", "승인 대기"),
    REJECTED("STATUS_REJECTED", "거부");

    private final String key;
    private final String title;
}

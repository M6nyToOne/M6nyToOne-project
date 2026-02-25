package sparta.m6nytooneproject.order.entity;

import lombok.Getter;

@Getter
public enum OrderSort {
    PRICE("price"),
    QUANTITY("quantity"),
    CREATED_AT("createdAt"); // ✅ 엔티티 필드명으로!

    private final String property;

    OrderSort(String property) {
        this.property = property;
    }
}
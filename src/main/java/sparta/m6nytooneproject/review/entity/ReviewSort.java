package sparta.m6nytooneproject.review.entity;

import lombok.Getter;

@Getter
public enum ReviewSort {
    REVIEWRATE("reviewRate"),
    CREATED_AT("createdAt"); // ✅ 엔티티 필드명으로!

    private final String property;

    ReviewSort(String property) {
        this.property = property;
    }
}

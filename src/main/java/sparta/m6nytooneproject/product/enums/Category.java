package sparta.m6nytooneproject.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    ELECTRONICS("전자기기"),
    FASHION("패션/의류"),
    FOOD("식품");

    private final String category;
}

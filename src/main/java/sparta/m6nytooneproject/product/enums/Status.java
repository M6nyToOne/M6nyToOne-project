package sparta.m6nytooneproject.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {

    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    DISCONTINUED("단종");

    private final String status;
}

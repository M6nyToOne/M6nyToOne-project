package sparta.m6nytooneproject.product.entity;

import lombok.Getter;

@Getter
public enum ProductSort {
    PRICE("price"),
    STOCK("stock"),
    CREATED_AT("createdAt");

    private final String property;

    ProductSort(String property) {this.property = property;}
}

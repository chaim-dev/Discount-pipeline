package com.carrefour.discount.exceptions;

public class DiscountNotFoundException extends RuntimeException {
    public DiscountNotFoundException(String code) {
        super("Discount code not found: " + code);
    }
}

package com.carrefour.discount.exceptions;

public class DiscountInvalidException extends RuntimeException {
    public DiscountInvalidException(String message) {
        super(message);
    }
}

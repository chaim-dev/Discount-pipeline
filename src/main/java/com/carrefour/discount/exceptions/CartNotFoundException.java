package com.carrefour.discount.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long cartId) {
        super("Cart not found for id: " + cartId);
    }
}

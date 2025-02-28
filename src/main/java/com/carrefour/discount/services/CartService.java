package com.carrefour.discount.services;

import com.carrefour.discount.entities.Cart;
import com.carrefour.discount.exceptions.CartNotFoundException;
import com.carrefour.discount.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart createCart(Cart cart) {
        cart.getItems().forEach(item -> item.setCart(cart));
        return cartRepository.save(cart);
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
    }

    public Cart save(Cart cart) {
        cart.getItems().forEach(item -> item.setCart(cart));
        return cartRepository.save(cart);
    }
}

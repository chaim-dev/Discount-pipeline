package com.carrefour.discount.controllers;

import com.carrefour.discount.entities.Cart;
import com.carrefour.discount.entities.Discount;
import com.carrefour.discount.services.CartService;
import com.carrefour.discount.services.DiscountService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        Cart saved = cartService.createCart(cart);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/{cartId}/apply-discount")
    public ResponseEntity<Cart> applyDiscount(@PathVariable Long cartId, @RequestParam String code) {
        Cart cart = cartService.getCartById(cartId);

        Discount discount = discountService.validateDiscountCode(code);

        discountService.applyDiscount(cart, discount);

        Cart updatedCart = cartService.save(cart);

        return ResponseEntity.ok(updatedCart);
    }
}

package com.carrefour.discount.services;

import com.carrefour.discount.entities.Cart;
import com.carrefour.discount.entities.CartItem;
import com.carrefour.discount.entities.Discount;
import com.carrefour.discount.entities.Product;
import com.carrefour.discount.exceptions.DiscountNotFoundException;
import com.carrefour.discount.exceptions.InvalidDiscountException;
import com.carrefour.discount.repositories.DiscountRepository;
import com.carrefour.discount.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public Discount validateDiscountCode(String code) {
        Discount discount = discountRepository.findByCode(code)
                .orElseThrow(() -> new DiscountNotFoundException(code));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(discount.getValidFrom()) || now.isAfter(discount.getValidTo())) {
            throw new InvalidDiscountException("Discount code " + code + " is expired or not yet valid.");
        }
        return discount;
    }

    public void applyDiscount(Cart cart, Discount discount) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            BigDecimal itemSubtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            if (product.isDiscountable()) {
                BigDecimal discountAmount = itemSubtotal
                        .multiply(BigDecimal.valueOf(discount.getPercentage()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                itemSubtotal = itemSubtotal.subtract(discountAmount);
            }

            total = total.add(itemSubtotal);
        }

        cart.setTotalPrice(total);
        cart.setDiscountCode(discount.getCode());
    }
}

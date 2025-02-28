package com.carrefour.discount.controllers;


import com.carrefour.discount.entities.Cart;
import com.carrefour.discount.entities.Discount;
import com.carrefour.discount.exceptions.CartNotFoundException;
import com.carrefour.discount.exceptions.DiscountNotFoundException;
import com.carrefour.discount.exceptions.InvalidDiscountException;
import com.carrefour.discount.services.CartService;
import com.carrefour.discount.services.DiscountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CartService cartService;

    @MockitoBean
    DiscountService discountService;

    Cart testCart;

    @BeforeEach
    void setUp() {
        testCart = new Cart();
        testCart.setId(10L);
        testCart.setItems(List.of());
        testCart.setTotalPrice(BigDecimal.ZERO);
    }

    @Test
    void getCart_ok() throws Exception {
        when(cartService.getCartById(10L)).thenReturn(testCart);
        mockMvc.perform(get("/api/carts/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void getCart_notFound() throws Exception {
        when(cartService.getCartById(999L)).thenThrow(new CartNotFoundException(999L));
        mockMvc.perform(get("/api/carts/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Cart not found for id: 999")));
    }

    @Test
    void applyDiscount_ok() throws Exception {
        when(cartService.getCartById(10L)).thenReturn(testCart);
        Discount discount = new Discount();
        discount.setCode("SUMMER2025");
        when(discountService.validateDiscountCode("SUMMER2025")).thenReturn(discount);
        doAnswer(inv -> {
            Cart c = inv.getArgument(0);
            c.setDiscountCode("SUMMER2025");
            c.setTotalPrice(BigDecimal.valueOf(45.00));
            return null;
        }).when(discountService).applyDiscount(any(Cart.class), eq(discount));
        when(cartService.save(any(Cart.class))).thenReturn(testCart);
        mockMvc.perform(post("/api/carts/10/apply-discount?code=SUMMER2025")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountCode").value("SUMMER2025"))
                .andExpect(jsonPath("$.totalPrice").value(45.0));
    }

    @Test
    void applyDiscount_discountNotFound() throws Exception {
        when(cartService.getCartById(10L)).thenReturn(testCart);
        when(discountService.validateDiscountCode("UNKNOWN"))
                .thenThrow(new DiscountNotFoundException("UNKNOWN"));
        mockMvc.perform(post("/api/carts/10/apply-discount?code=UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Discount code not found: UNKNOWN")));
    }

    @Test
    void applyDiscount_invalidDiscount() throws Exception {
        when(cartService.getCartById(10L)).thenReturn(testCart);
        when(discountService.validateDiscountCode("EXPIRED"))
                .thenThrow(new InvalidDiscountException("Discount code EXPIRED is expired or not yet valid."));
        mockMvc.perform(post("/api/carts/10/apply-discount?code=EXPIRED"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("expired or not yet valid")));
    }
}

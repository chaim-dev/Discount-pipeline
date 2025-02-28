package com.carrefour.discount.services;

import com.carrefour.discount.entities.Cart;
import com.carrefour.discount.entities.CartItem;
import com.carrefour.discount.exceptions.CartNotFoundException;
import com.carrefour.discount.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private Cart sampleCart;

    @BeforeEach
    void setUp() {
        CartItem item = CartItem.builder()
                .id(101L)
                .productId(1L)
                .quantity(2)
                .build();

        sampleCart = Cart.builder()
                .id(10L)
                .items(List.of(item))
                .totalPrice(BigDecimal.ZERO)
                .build();
    }

    @Test
    void getCartById_ok() {
        when(cartRepository.findById(10L)).thenReturn(Optional.of(sampleCart));

        Cart cart = cartService.getCartById(10L);

        assertEquals(10L, cart.getId());
        verify(cartRepository, times(1)).findById(10L);
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    void getCartById_notFound() {
        when(cartRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getCartById(999L));
        verify(cartRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    void saveCart_ok() {
        when(cartRepository.save(sampleCart)).thenReturn(sampleCart);

        Cart saved = cartService.save(sampleCart);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        verify(cartRepository, times(1)).save(sampleCart);
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    void createCart_ok() {
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart c = invocation.getArgument(0);
            c.setId(11L);
            return c;
        });

        Cart created = cartService.createCart(sampleCart);

        assertNotNull(created.getId());
        assertEquals(11L, created.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
        verifyNoMoreInteractions(cartRepository);
    }
}

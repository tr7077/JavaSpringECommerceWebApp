package com.teorerras.buynowdotcom.service.cart;

import com.teorerras.buynowdotcom.dtos.CartDto;
import com.teorerras.buynowdotcom.model.Cart;
import com.teorerras.buynowdotcom.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);
    Cart getCartByUserId(Long userId);
    void clearCart(Long cartId);
    Cart initializeNewCartForUser(User user);
    BigDecimal getTotalPrice(Long cartId);

    CartDto convertToDto(Cart cart);
}

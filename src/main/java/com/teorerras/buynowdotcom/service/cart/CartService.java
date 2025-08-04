package com.teorerras.buynowdotcom.service.cart;

import com.teorerras.buynowdotcom.dtos.CartDto;
import com.teorerras.buynowdotcom.dtos.UserDto;
import com.teorerras.buynowdotcom.model.Cart;
import com.teorerras.buynowdotcom.model.User;
import com.teorerras.buynowdotcom.repository.CartItemRepository;
import com.teorerras.buynowdotcom.repository.CartRepository;
import com.teorerras.buynowdotcom.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        cart.updateTotalAmount();
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        // UserService userService = null;
        // return userService.getUserById(userId).getCart();
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.updateTotalAmount();
            return cartRepository.save(cart);
        }
        System.out.println("cart is null");
        return null;
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cart.clearCart();
        cartRepository.deleteById(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        // cartRepository.save(cart);
    }

    @Override
    public Cart initializeNewCartForUser(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        return cartDto;
    }
}

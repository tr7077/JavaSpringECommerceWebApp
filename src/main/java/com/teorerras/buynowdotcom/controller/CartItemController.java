package com.teorerras.buynowdotcom.controller;

import com.teorerras.buynowdotcom.dtos.CartItemDto;
import com.teorerras.buynowdotcom.model.Cart;
import com.teorerras.buynowdotcom.model.CartItem;
import com.teorerras.buynowdotcom.model.User;
import com.teorerras.buynowdotcom.response.ApiResponse;
import com.teorerras.buynowdotcom.service.cart.ICartItemService;
import com.teorerras.buynowdotcom.service.cart.ICartService;
import com.teorerras.buynowdotcom.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        User user = userService.getAuthenticatedUser(); // userId
        Cart userCart = cartService.initializeNewCartForUser(user);
        CartItem newCartItem = cartItemService.addItemToCart(userCart.getId(), productId, quantity);
        CartItemDto cartItemDto = cartItemService.convertToDto(newCartItem);
        return ResponseEntity.ok(new ApiResponse("Item added successfully", cartItemDto));
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        cartItemService.removeItemFromCart(cartId, itemId); // productId ???
        return ResponseEntity.ok(new ApiResponse("Item removed from cart", null));
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam int quantity) {
        cartItemService.updateItemQuantity(cartId, itemId, quantity); // productId ???
        return ResponseEntity.ok(new ApiResponse("Item updated successfully", null));
    }
}

package com.teorerras.buynowdotcom.service.order;

import com.teorerras.buynowdotcom.dtos.OrderDto;
import com.teorerras.buynowdotcom.dtos.OrderItemDto;
import com.teorerras.buynowdotcom.enums.OrderStatus;
import com.teorerras.buynowdotcom.model.Cart;
import com.teorerras.buynowdotcom.model.Order;
import com.teorerras.buynowdotcom.model.OrderItem;
import com.teorerras.buynowdotcom.model.Product;
import com.teorerras.buynowdotcom.repository.OrderRepository;
import com.teorerras.buynowdotcom.repository.ProductRepository;
import com.teorerras.buynowdotcom.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        System.out.println("going to get the order list");
        List<Order> orders = orderRepository.findByUserId(userId);
        System.out.println("got order list from repo");
        return orders.stream().map(this :: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
//        List<OrderItemDto> orderItemDtoList = order.getItems().stream()
//                .map(orderItem -> {
//                    return new OrderItemDto(
//                            orderItem.getProduct().getId(),
//                            orderItem.getProduct().getName(),
//                            orderItem.getProduct().getBrand(),
//                            orderItem.getQuantity(),
//                            orderItem.getPrice()
//                    );
//                }).toList();
//        OrderDto orderDto = new OrderDto(
//                order.getId(),
//                order.getUser().getId(),
//                order.getOrderDate(),
//                order.getTotalAmount(),
//                order.getOrderStatus().name(),
//                orderItemDtoList
//        );
//        return orderDto;
    }
}

package com.teorerras.buynowdotcom.service.order;

import com.teorerras.buynowdotcom.dtos.OrderDto;
import com.teorerras.buynowdotcom.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}

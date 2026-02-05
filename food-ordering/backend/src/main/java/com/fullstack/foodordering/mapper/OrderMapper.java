package com.fullstack.foodordering.mapper;

import com.fullstack.foodordering.dto.OrderDto;
import com.fullstack.foodordering.dto.OrderItemDto;
import com.fullstack.foodordering.entity.Order;
import com.fullstack.foodordering.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) return null;
        
        OrderDto.OrderDtoBuilder builder = OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt());
        
        if (order.getRestaurant() != null) {
            builder.restaurantId(order.getRestaurant().getId())
                   .restaurantName(order.getRestaurant().getName());
        }
        
        if (order.getItems() != null) {
            builder.items(order.getItems().stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList()));
        }
        
        return builder.build();
    }

    private OrderItemDto toItemDto(OrderItem item) {
        if (item == null) return null;
        
        OrderItemDto.OrderItemDtoBuilder builder = OrderItemDto.builder()
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice());
        
        if (item.getMenuItem() != null) {
            builder.menuItemName(item.getMenuItem().getName());
        }
        
        // Calculate subtotal
        if (item.getUnitPrice() != null && item.getQuantity() != null) {
            builder.subtotal(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
        }
        
        return builder.build();
    }
}

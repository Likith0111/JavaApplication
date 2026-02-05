package com.fullstack.foodordering.mapper;

import com.fullstack.foodordering.dto.RestaurantDto;
import com.fullstack.foodordering.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public RestaurantDto toDto(Restaurant restaurant) {
        if (restaurant == null) return null;
        
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .build();
    }

    public Restaurant toEntity(RestaurantDto dto) {
        if (dto == null) return null;
        
        return Restaurant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .build();
    }
}

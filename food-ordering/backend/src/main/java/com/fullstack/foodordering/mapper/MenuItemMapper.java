package com.fullstack.foodordering.mapper;

import com.fullstack.foodordering.dto.MenuItemDto;
import com.fullstack.foodordering.entity.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItemDto toDto(MenuItem menuItem) {
        if (menuItem == null) return null;
        
        MenuItemDto.MenuItemDtoBuilder builder = MenuItemDto.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice());
        
        if (menuItem.getRestaurant() != null) {
            builder.restaurantId(menuItem.getRestaurant().getId());
        }
        
        return builder.build();
    }

    public MenuItem toEntity(MenuItemDto dto) {
        if (dto == null) return null;
        
        return MenuItem.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}

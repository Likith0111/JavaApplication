package com.fullstack.ecommerce.mapper;

import com.fullstack.ecommerce.dto.CategoryDto;
import com.fullstack.ecommerce.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category c) {
        if (c == null) return null;
        return CategoryDto.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .build();
    }
}

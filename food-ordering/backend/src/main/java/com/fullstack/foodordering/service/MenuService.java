package com.fullstack.foodordering.service;

import com.fullstack.foodordering.dto.MenuItemDto;
import com.fullstack.foodordering.entity.MenuItem;
import com.fullstack.foodordering.entity.Restaurant;
import com.fullstack.foodordering.exception.ResourceNotFoundException;
import com.fullstack.foodordering.repository.MenuItemRepository;
import com.fullstack.foodordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing menu item operations for the food ordering platform.
 * 
 * <p>This service provides comprehensive menu management functionality including
 * creating, reading, updating, and deleting menu items. Menu items are organized
 * by restaurants and represent the food items available for ordering.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Complete CRUD operations for menu items</li>
 *   <li>Restaurant-specific menu item retrieval</li>
 *   <li>Menu item creation with restaurant association</li>
 *   <li>Menu item updates with validation</li>
 *   <li>Menu item deletion with existence validation</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Menu items must be associated with a valid restaurant</li>
 *   <li>All menu operations are transactional to ensure data integrity</li>
 *   <li>Menu items are retrieved filtered by restaurant for customer browsing</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    /** Repository for menu item persistence operations */
    private final MenuItemRepository menuItemRepository;
    
    /** Repository for restaurant validation during menu item creation */
    private final RestaurantRepository restaurantRepository;

    /**
     * Retrieves all menu items for a specific restaurant.
     * 
     * <p>This method fetches all menu items associated with a restaurant,
     * enabling customers to browse the restaurant's menu.</p>
     * 
     * @param restaurantId the unique identifier of the restaurant
     * @return list of {@link MenuItemDto} objects representing all menu items for the restaurant
     * 
     * @see MenuItemDto
     */
    public List<MenuItemDto> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific menu item by its unique identifier.
     * 
     * @param id the unique identifier of the menu item to retrieve
     * @return {@link MenuItemDto} containing the menu item details
     * @throws ResourceNotFoundException if the menu item with the given ID does not exist
     * 
     * @see MenuItemDto
     */
    public MenuItemDto getById(Long id) {
        MenuItem m = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
        return toDto(m);
    }

    /**
     * Creates a new menu item for a restaurant.
     * 
     * <p>This method creates a new menu item with the provided details. The menu item
     * must be associated with a valid restaurant.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that menu item creation is atomic and consistent.</p>
     * 
     * @param restaurantId the unique identifier of the restaurant this menu item belongs to
     * @param name the name of the menu item (required)
     * @param description the description of the menu item
     * @param price the price of the menu item (required, uses BigDecimal for precision)
     * @return {@link MenuItemDto} containing the created menu item details including generated ID
     * @throws ResourceNotFoundException if the restaurant with the given ID does not exist
     * 
     * @see MenuItemDto
     */
    @Transactional
    public MenuItemDto create(Long restaurantId, String name, String description, BigDecimal price) {
        // Validate restaurant exists
        Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
        
        // Build menu item entity with provided details
        MenuItem m = MenuItem.builder().name(name).description(description).price(price).restaurant(r).build();
        
        // Persist menu item to database
        m = menuItemRepository.save(m);
        
        // Convert to DTO and return
        return toDto(m);
    }

    /**
     * Updates an existing menu item's details.
     * 
     * <p>This method allows modification of menu item information including name,
     * description, and price.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that menu item updates are atomic and consistent.</p>
     * 
     * @param id the unique identifier of the menu item to update
     * @param name the new name for the menu item
     * @param description the new description for the menu item
     * @param price the new price for the menu item
     * @return {@link MenuItemDto} containing the updated menu item details
     * @throws ResourceNotFoundException if the menu item with the given ID does not exist
     * 
     * @see MenuItemDto
     */
    @Transactional
    public MenuItemDto update(Long id, String name, String description, BigDecimal price) {
        // Retrieve menu item and validate existence
        MenuItem m = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
        
        // Update menu item properties
        m.setName(name);
        m.setDescription(description);
        m.setPrice(price);
        
        // Persist changes
        m = menuItemRepository.save(m);
        
        // Convert to DTO and return
        return toDto(m);
    }

    /**
     * Deletes a menu item from the system.
     * 
     * <p>This method permanently removes a menu item after validating its existence.
     * The operation is transactional to ensure data consistency.</p>
     * 
     * <p><b>Note:</b> Consider the impact on existing orders and cart items before
     * deleting a menu item. Items referenced in orders should typically be soft-deleted
     * or marked as inactive rather than hard-deleted.</p>
     * 
     * @param id the unique identifier of the menu item to delete
     * @throws ResourceNotFoundException if the menu item with the given ID does not exist
     */
    @Transactional
    public void delete(Long id) {
        // Validate menu item existence before deletion
        if (!menuItemRepository.existsById(id)) throw new ResourceNotFoundException("MenuItem", id);
        
        // Permanently delete the menu item
        menuItemRepository.deleteById(id);
    }

    /**
     * Converts a MenuItem entity to its corresponding DTO.
     * 
     * <p>This helper method transforms the entity to a DTO for client consumption.</p>
     * 
     * @param m the menu item entity to convert
     * @return {@link MenuItemDto} containing all menu item details
     */
    private MenuItemDto toDto(MenuItem m) {
        // Build and return DTO with all menu item details
        return MenuItemDto.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .price(m.getPrice())
                .restaurantId(m.getRestaurant().getId())
                .build();
    }
}

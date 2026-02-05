package com.fullstack.foodordering.service;

import com.fullstack.foodordering.dto.RestaurantDto;
import com.fullstack.foodordering.entity.Restaurant;
import com.fullstack.foodordering.exception.ResourceNotFoundException;
import com.fullstack.foodordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing restaurant operations for the food ordering platform.
 * 
 * <p>This service provides comprehensive restaurant management functionality including
 * creating, reading, updating, and deleting restaurants. Restaurants serve as the
 * primary organizational unit for menu items and orders.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Complete CRUD operations for restaurants</li>
 *   <li>Restaurant listing and retrieval by ID</li>
 *   <li>Restaurant creation with name, description, and address</li>
 *   <li>Restaurant updates with validation</li>
 *   <li>Restaurant deletion with existence validation</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Restaurants serve as containers for menu items</li>
 *   <li>Orders are associated with specific restaurants</li>
 *   <li>All restaurant operations are transactional to ensure data integrity</li>
 *   <li>Restaurant existence is validated before update and delete operations</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class RestaurantService {

    /** Repository for restaurant persistence operations */
    private final RestaurantRepository restaurantRepository;

    /**
     * Retrieves all restaurants in the system.
     * 
     * <p>This method fetches all available restaurants and converts them to DTOs
     * for client consumption. Useful for populating restaurant listings and
     * selection menus.</p>
     * 
     * @return list of {@link RestaurantDto} objects representing all restaurants
     * 
     * @see RestaurantDto
     */
    public List<RestaurantDto> findAll() {
        return restaurantRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific restaurant by its unique identifier.
     * 
     * @param id the unique identifier of the restaurant to retrieve
     * @return {@link RestaurantDto} containing the restaurant details
     * @throws ResourceNotFoundException if the restaurant with the given ID does not exist
     * 
     * @see RestaurantDto
     */
    public RestaurantDto getById(Long id) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        return toDto(r);
    }

    /**
     * Creates a new restaurant in the system.
     * 
     * <p>This method creates a new restaurant with the provided name, description,
     * and address. The restaurant can then be used to organize menu items and receive orders.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that restaurant creation is atomic and consistent.</p>
     * 
     * @param name the name of the restaurant (required)
     * @param description the description of the restaurant
     * @param address the physical address of the restaurant
     * @return {@link RestaurantDto} containing the created restaurant details including generated ID
     * 
     * @see RestaurantDto
     */
    @Transactional
    public RestaurantDto create(String name, String description, String address) {
        // Build new restaurant entity
        Restaurant r = Restaurant.builder().name(name).description(description).address(address).build();
        
        // Persist restaurant to database
        r = restaurantRepository.save(r);
        
        // Convert to DTO and return
        return toDto(r);
    }

    /**
     * Updates an existing restaurant's details.
     * 
     * <p>This method allows modification of restaurant information including name,
     * description, and address. The restaurant must exist in the system before it can be updated.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that restaurant updates are atomic and consistent.</p>
     * 
     * @param id the unique identifier of the restaurant to update
     * @param name the new name for the restaurant
     * @param description the new description for the restaurant
     * @param address the new address for the restaurant
     * @return {@link RestaurantDto} containing the updated restaurant details
     * @throws ResourceNotFoundException if the restaurant with the given ID does not exist
     * 
     * @see RestaurantDto
     */
    @Transactional
    public RestaurantDto update(Long id, String name, String description, String address) {
        // Retrieve restaurant and validate existence
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        
        // Update restaurant properties
        r.setName(name);
        r.setDescription(description);
        r.setAddress(address);
        
        // Persist changes
        r = restaurantRepository.save(r);
        
        // Convert to DTO and return
        return toDto(r);
    }

    /**
     * Deletes a restaurant from the system.
     * 
     * <p>This method permanently removes a restaurant after validating its existence.
     * The operation is transactional to ensure data consistency.</p>
     * 
     * <p><b>Note:</b> Consider the impact on associated menu items and orders before
     * deleting a restaurant. Restaurants referenced in orders should typically be
     * soft-deleted or marked as inactive rather than hard-deleted.</p>
     * 
     * @param id the unique identifier of the restaurant to delete
     * @throws ResourceNotFoundException if the restaurant with the given ID does not exist
     */
    @Transactional
    public void delete(Long id) {
        // Validate restaurant existence before deletion
        if (!restaurantRepository.existsById(id)) throw new ResourceNotFoundException("Restaurant", id);
        
        // Permanently delete the restaurant
        restaurantRepository.deleteById(id);
    }

    /**
     * Converts a Restaurant entity to its corresponding DTO.
     * 
     * <p>This helper method transforms the entity to a DTO for client consumption.</p>
     * 
     * @param r the restaurant entity to convert
     * @return {@link RestaurantDto} containing all restaurant details
     */
    private RestaurantDto toDto(Restaurant r) {
        return RestaurantDto.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .address(r.getAddress())
                .build();
    }
}

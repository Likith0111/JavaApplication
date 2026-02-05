package com.fullstack.foodordering.service;

import com.fullstack.foodordering.dto.CartItemDto;
import com.fullstack.foodordering.entity.CartItem;
import com.fullstack.foodordering.entity.MenuItem;
import com.fullstack.foodordering.entity.User;
import com.fullstack.foodordering.exception.BadRequestException;
import com.fullstack.foodordering.exception.ResourceNotFoundException;
import com.fullstack.foodordering.repository.CartItemRepository;
import com.fullstack.foodordering.repository.MenuItemRepository;
import com.fullstack.foodordering.repository.UserRepository;
import com.fullstack.foodordering.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing shopping cart operations for the food ordering platform.
 * 
 * <p>This service provides comprehensive cart management functionality including
 * adding menu items, removing items, and retrieving cart contents. It implements
 * secure user-specific cart operations with automatic subtotal calculations.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>User-specific cart isolation using Spring Security authentication</li>
 *   <li>Automatic quantity aggregation for duplicate menu item additions</li>
 *   <li>Automatic subtotal calculation for each cart item</li>
 *   <li>Transaction management for data consistency</li>
 *   <li>Authorization checks to prevent cart tampering</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Cart items are automatically merged when the same menu item is added multiple times</li>
 *   <li>Cart items are user-specific and cannot be accessed by other users</li>
 *   <li>Cart items are restaurant-specific and filtered during order placement</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class CartService {

    /** Repository for cart item persistence operations */
    private final CartItemRepository cartItemRepository;
    
    /** Repository for menu item data retrieval */
    private final MenuItemRepository menuItemRepository;
    
    /** Repository for user data retrieval */
    private final UserRepository userRepository;

    /**
     * Retrieves all cart items for the currently authenticated user.
     * 
     * <p>This method fetches all cart items associated with the logged-in user
     * and converts them to DTOs with calculated subtotals. The cart items are
     * returned in the order they were added.</p>
     * 
     * @return list of {@link CartItemDto} objects representing all items in the user's cart,
     *         each containing menu item details, quantity, unit price, and calculated subtotal
     * 
     * @see CartItemDto
     */
    public List<CartItemDto> getCart() {
        Long userId = getCurrentUserId();
        return cartItemRepository.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Adds a menu item to the user's shopping cart or updates quantity if already present.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates menu item existence and retrieves menu item details</li>
     *   <li>Checks if the menu item already exists in the user's cart</li>
     *   <li>If exists: aggregates quantities</li>
     *   <li>If new: creates a new cart item entry</li>
     *   <li>Persists the cart item and returns updated DTO</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that cart updates are atomic and consistent.</p>
     * 
     * @param menuItemId the unique identifier of the menu item to add to cart
     * @param quantity the number of units to add (must be positive)
     * @return {@link CartItemDto} containing the cart item details with calculated subtotal
     * @throws ResourceNotFoundException if the menu item or user does not exist
     * 
     * @see CartItemDto
     */
    @Transactional
    public CartItemDto addToCart(Long menuItemId, int quantity) {
        // Get current authenticated user ID
        Long userId = getCurrentUserId();
        
        // Retrieve user entity - required for cart item association
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        // Retrieve menu item entity and validate existence
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ResourceNotFoundException("MenuItem", menuItemId));
        
        // Check if menu item already exists in user's cart
        CartItem item = cartItemRepository.findByUserIdAndMenuItemId(userId, menuItemId).orElse(null);
        
        if (item != null) {
            // Menu item already in cart - aggregate quantities
            item.setQuantity(item.getQuantity() + quantity);
            item = cartItemRepository.save(item);
        } else {
            // Menu item not in cart - create new cart item
            item = CartItem.builder().user(user).menuItem(menuItem).quantity(quantity).build();
            item = cartItemRepository.save(item);
        }
        
        return toDto(item);
    }

    /**
     * Removes a specific item from the user's shopping cart.
     * 
     * <p>This method permanently deletes a cart item after validating that it
     * belongs to the currently authenticated user. The operation is transactional
     * to ensure data consistency.</p>
     * 
     * <p><b>Authorization:</b> Only the owner of the cart item can remove it.
     * Attempts to remove other users' cart items will result in a BadRequestException.</p>
     * 
     * @param cartItemId the unique identifier of the cart item to remove
     * @throws ResourceNotFoundException if the cart item does not exist
     * @throws BadRequestException if the cart item does not belong to the current user
     */
    @Transactional
    public void removeFromCart(Long cartItemId) {
        // Retrieve cart item and validate existence
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("CartItem", cartItemId));
        
        // Authorization check - ensure user owns this cart item
        if (!item.getUser().getId().equals(getCurrentUserId())) throw new BadRequestException("Not your cart item");
        
        // Permanently delete the cart item
        cartItemRepository.delete(item);
    }

    /**
     * Retrieves the ID of the currently authenticated user from Spring Security context.
     * 
     * @return the unique identifier of the authenticated user
     */
    private Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    /**
     * Converts a CartItem entity to its corresponding DTO with calculated subtotal.
     * 
     * <p>This helper method transforms the entity to a DTO and calculates the
     * subtotal by multiplying the menu item price by the quantity.</p>
     * 
     * @param item the cart item entity to convert
     * @return {@link CartItemDto} containing all cart item details with calculated subtotal
     */
    private CartItemDto toDto(CartItem item) {
        // Calculate subtotal: unit price × quantity
        BigDecimal subtotal = item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        
        // Build and return DTO with all cart item details
        return CartItemDto.builder()
                .id(item.getId())
                .menuItemId(item.getMenuItem().getId())
                .menuItemName(item.getMenuItem().getName())
                .unitPrice(item.getMenuItem().getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}

package com.fullstack.ecommerce.service;

import com.fullstack.ecommerce.dto.CartItemDto;
import com.fullstack.ecommerce.entity.CartItem;
import com.fullstack.ecommerce.entity.Product;
import com.fullstack.ecommerce.entity.User;
import com.fullstack.ecommerce.exception.BadRequestException;
import com.fullstack.ecommerce.exception.ResourceNotFoundException;
import com.fullstack.ecommerce.repository.CartItemRepository;
import com.fullstack.ecommerce.repository.ProductRepository;
import com.fullstack.ecommerce.repository.UserRepository;
import com.fullstack.ecommerce.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing shopping cart operations for the e-commerce platform.
 * 
 * <p>This service provides comprehensive cart management functionality including
 * adding items, updating quantities, removing items, and retrieving cart contents.
 * It implements secure user-specific cart operations with stock validation and
 * automatic subtotal calculations.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>User-specific cart isolation using Spring Security authentication</li>
 *   <li>Automatic quantity aggregation for duplicate product additions</li>
 *   <li>Real-time stock availability validation</li>
 *   <li>Automatic subtotal calculation for each cart item</li>
 *   <li>Transaction management for data consistency</li>
 *   <li>Authorization checks to prevent cart tampering</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Cart items are automatically merged when the same product is added multiple times</li>
 *   <li>Stock validation prevents adding more items than available</li>
 *   <li>Cart items are user-specific and cannot be accessed by other users</li>
 *   <li>Quantity updates automatically remove items if quantity is set to zero</li>
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
    
    /** Repository for product data retrieval and stock validation */
    private final ProductRepository productRepository;
    
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
     *         each containing product details, quantity, unit price, and calculated subtotal
     * 
     * @see CartItemDto
     */
    public List<CartItemDto> getCart() {
        Long userId = getCurrentUserId();
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds a product to the user's shopping cart or updates quantity if already present.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates product existence and retrieves product details</li>
     *   <li>Checks stock availability against requested quantity</li>
     *   <li>Checks if the product already exists in the user's cart</li>
     *   <li>If exists: aggregates quantities and validates total against stock</li>
     *   <li>If new: creates a new cart item entry</li>
     *   <li>Persists the cart item and returns updated DTO</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that cart updates are atomic and consistent.</p>
     * 
     * <p><b>Stock Validation:</b> The method validates stock availability both
     * for new additions and quantity updates to prevent overselling.</p>
     * 
     * @param productId the unique identifier of the product to add to cart
     * @param quantity the number of units to add (must be positive)
     * @return {@link CartItemDto} containing the cart item details with calculated subtotal
     * @throws ResourceNotFoundException if the product or user does not exist
     * @throws BadRequestException if requested quantity exceeds available stock
     * 
     * @see CartItemDto
     */
    @Transactional
    public CartItemDto addToCart(Long productId, int quantity) {
        // Get current authenticated user ID
        Long userId = getCurrentUserId();
        
        // Retrieve user entity - required for cart item association
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        // Retrieve product entity and validate existence
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        
        // Validate stock availability for initial quantity check
        if (product.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock");
        }
        
        // Check if product already exists in user's cart
        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId).orElse(null);
        
        if (item != null) {
            // Product already in cart - aggregate quantities
            int newQty = item.getQuantity() + quantity;
            
            // Validate aggregated quantity against available stock
            if (product.getStock() < newQty) throw new BadRequestException("Insufficient stock");
            
            // Update existing cart item quantity
            item.setQuantity(newQty);
            item = cartItemRepository.save(item);
        } else {
            // Product not in cart - create new cart item
            item = CartItem.builder().user(user).product(product).quantity(quantity).build();
            item = cartItemRepository.save(item);
        }
        
        return toDto(item);
    }

    /**
     * Updates the quantity of a specific cart item.
     * 
     * <p>This method allows users to modify the quantity of items in their cart.
     * If the quantity is set to zero or negative, the item is automatically removed
     * from the cart. The method validates stock availability and ensures the cart
     * item belongs to the current user.</p>
     * 
     * <p><b>Authorization:</b> Only the owner of the cart item can update its quantity.
     * Attempts to modify other users' cart items will result in a BadRequestException.</p>
     * 
     * <p><b>Auto-Removal:</b> If quantity is set to less than 1, the cart item
     * is automatically deleted, effectively removing it from the cart.</p>
     * 
     * @param cartItemId the unique identifier of the cart item to update
     * @param quantity the new quantity (if less than 1, item is removed from cart)
     * @throws ResourceNotFoundException if the cart item does not exist
     * @throws BadRequestException if the cart item does not belong to the current user
     *                              or if the requested quantity exceeds available stock
     */
    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {
        // Retrieve cart item and validate existence
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("CartItem", cartItemId));
        
        // Authorization check - ensure user owns this cart item
        if (!item.getUser().getId().equals(getCurrentUserId())) {
            throw new BadRequestException("Not your cart item");
        }
        
        // Auto-remove item if quantity is zero or negative
        if (quantity < 1) {
            cartItemRepository.delete(item);
            return;
        }
        
        // Validate stock availability for new quantity
        if (item.getProduct().getStock() < quantity) throw new BadRequestException("Insufficient stock");
        
        // Update quantity and persist changes
        item.setQuantity(quantity);
        cartItemRepository.save(item);
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
     * subtotal by multiplying the product price by the quantity.</p>
     * 
     * @param item the cart item entity to convert
     * @return {@link CartItemDto} containing all cart item details with calculated subtotal
     */
    private CartItemDto toDto(CartItem item) {
        // Calculate subtotal: unit price × quantity
        BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        
        // Build and return DTO with all cart item details
        return CartItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .unitPrice(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}

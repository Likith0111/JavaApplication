package com.fullstack.foodordering.service;

import com.fullstack.foodordering.dto.OrderDto;
import com.fullstack.foodordering.dto.OrderItemDto;
import com.fullstack.foodordering.entity.*;
import com.fullstack.foodordering.exception.BadRequestException;
import com.fullstack.foodordering.exception.ResourceNotFoundException;
import com.fullstack.foodordering.repository.*;
import com.fullstack.foodordering.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing order operations for the food ordering platform.
 * 
 * <p>This service provides comprehensive order management functionality including
 * order creation from cart items, order retrieval, order history, and order status
 * updates. It handles the complete order lifecycle from cart checkout to order fulfillment.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Restaurant-specific cart-to-order conversion</li>
 *   <li>Order total calculation based on menu item prices and quantities</li>
 *   <li>User-specific order retrieval and history</li>
 *   <li>Order status management (CREATED, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED)</li>
 *   <li>Pagination support for order listings</li>
 *   <li>Transaction management ensuring atomic order creation</li>
 *   <li>Authorization checks to prevent order access violations</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Orders are created from cart items filtered by restaurant</li>
 *   <li>Cart is automatically cleared after successful order creation</li>
 *   <li>Order totals are calculated as sum of (unit price × quantity) for all items</li>
 *   <li>Orders are restaurant-specific - users can only order from one restaurant per order</li>
 *   <li>Order status can be updated by authorized users (restaurant staff)</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    /** Repository for order persistence operations */
    private final OrderRepository orderRepository;
    
    /** Repository for order item persistence operations */
    private final OrderItemRepository orderItemRepository;
    
    /** Repository for cart item operations (used during order placement) */
    private final CartItemRepository cartItemRepository;
    
    /** Repository for user data retrieval */
    private final UserRepository userRepository;
    
    /** Repository for restaurant data retrieval */
    private final RestaurantRepository restaurantRepository;

    /**
     * Places an order by converting cart items for a specific restaurant into an order.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates restaurant existence</li>
     *   <li>Filters cart items to only include items from the specified restaurant</li>
     *   <li>Validates that cart is not empty for this restaurant</li>
     *   <li>Creates a new order with CREATED status</li>
     *   <li>Creates order items with menu item details and prices</li>
     *   <li>Calculates and sets the total order amount</li>
     *   <li>Clears the restaurant-specific cart items after successful order creation</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that order creation is atomic and consistent.</p>
     * 
     * <p><b>Restaurant Filtering:</b> Only cart items from the specified restaurant
     * are included in the order. This ensures users can order from multiple restaurants
     * by placing separate orders.</p>
     * 
     * @param restaurantId the unique identifier of the restaurant to place order from
     * @return {@link OrderDto} containing the created order details with all items and total
     * @throws BadRequestException if the cart is empty for the specified restaurant
     * @throws ResourceNotFoundException if the restaurant or user does not exist
     * 
     * @see OrderDto
     */
    @Transactional
    public OrderDto placeOrder(Long restaurantId) {
        // Get current authenticated user ID
        Long userId = getCurrentUserId();
        
        // Retrieve user entity - required for order association
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        // Retrieve restaurant entity and validate existence
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
        
        // Filter cart items to only include items from the specified restaurant
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId).stream()
                .filter(ci -> ci.getMenuItem().getRestaurant().getId().equals(restaurantId))
                .collect(Collectors.toList());
        
        // Validate cart is not empty for this restaurant
        if (cartItems.isEmpty()) throw new BadRequestException("Cart is empty for this restaurant");
        
        // Initialize order total
        BigDecimal total = BigDecimal.ZERO;
        
        // Create order with CREATED status (initial total will be updated after items are processed)
        Order order = Order.builder().user(user).restaurant(restaurant).status(OrderStatus.CREATED).totalAmount(BigDecimal.ZERO).build();
        order = orderRepository.save(order);
        
        // Process each cart item: create order item and accumulate total
        for (CartItem ci : cartItems) {
            // Create order item with menu item details and snapshot of price at time of order
            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .menuItem(ci.getMenuItem())
                    .quantity(ci.getQuantity())
                    .unitPrice(ci.getMenuItem().getPrice()) // Store price snapshot for order history
                    .build();
            oi = orderItemRepository.save(oi);
            
            // Accumulate total: unit price × quantity
            total = total.add(ci.getMenuItem().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            
            // Remove cart item after adding to order
            cartItemRepository.delete(ci);
        }
        
        // Update order with calculated total amount
        order.setTotalAmount(total);
        orderRepository.save(order);
        
        return toDto(order);
    }

    /**
     * Retrieves paginated order history for the currently authenticated user.
     * 
     * <p>This method fetches orders belonging to the logged-in user, sorted by
     * creation date in descending order (most recent first). Results are paginated
     * to support efficient retrieval of large order histories.</p>
     * 
     * @param page the page number (0-indexed)
     * @param size the number of orders per page
     * @return list of {@link OrderDto} objects representing user's orders,
     *         sorted by creation date descending
     * 
     * @see OrderDto
     */
    public List<OrderDto> getMyOrders(int page, int size) {
        Long userId = getCurrentUserId();
        
        // Create pagination request sorted by creation date (newest first)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        // Fetch paginated orders and convert to DTOs
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable).getContent().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific order by its unique identifier.
     * 
     * <p>This method fetches order details including all order items. Only the
     * owner of the order can access it, ensuring data privacy and security.</p>
     * 
     * <p><b>Authorization:</b> Only the user who placed the order can retrieve it.
     * Attempts to access other users' orders will result in a BadRequestException.</p>
     * 
     * @param id the unique identifier of the order to retrieve
     * @return {@link OrderDto} containing complete order details with all items
     * @throws ResourceNotFoundException if the order does not exist
     * @throws BadRequestException if the order does not belong to the current user
     * 
     * @see OrderDto
     */
    public OrderDto getById(Long id) {
        // Retrieve order and validate existence
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
        
        // Authorization check - ensure user owns this order
        if (!order.getUser().getId().equals(getCurrentUserId())) throw new BadRequestException("Not your order");
        
        return toDto(order);
    }

    /**
     * Updates the status of an existing order.
     * 
     * <p>This method allows authorized users (typically restaurant staff) to update
     * the order status as it progresses through the fulfillment lifecycle (CREATED,
     * CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED).</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that status updates are atomic and consistent.</p>
     * 
     * @param id the unique identifier of the order to update
     * @param status the new status to set for the order
     * @return {@link OrderDto} containing the updated order details
     * @throws ResourceNotFoundException if the order does not exist
     * 
     * @see OrderDto
     * @see OrderStatus
     */
    @Transactional
    public OrderDto updateStatus(Long id, OrderStatus status) {
        // Retrieve order and validate existence
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
        
        // Update order status
        order.setStatus(status);
        order = orderRepository.save(order);
        
        return toDto(order);
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
     * Converts an Order entity to its corresponding DTO with all order items.
     * 
     * <p>This helper method transforms the order entity to a DTO, including all
     * associated order items with calculated subtotals and restaurant information.</p>
     * 
     * @param order the order entity to convert
     * @return {@link OrderDto} containing complete order details with all items and totals
     */
    private OrderDto toDto(Order order) {
        // Retrieve all order items for this order
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        
        // Convert order items to DTOs with calculated subtotals
        List<OrderItemDto> itemDtos = items.stream().map(oi -> {
            // Calculate subtotal: unit price × quantity
            BigDecimal subtotal = oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
            
            return OrderItemDto.builder()
                    .menuItemName(oi.getMenuItem().getName())
                    .quantity(oi.getQuantity())
                    .unitPrice(oi.getUnitPrice())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());
        
        // Build and return order DTO with all details including restaurant information
        return OrderDto.builder()
                .id(order.getId())
                .restaurantId(order.getRestaurant().getId())
                .restaurantName(order.getRestaurant().getName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(itemDtos)
                .build();
    }
}

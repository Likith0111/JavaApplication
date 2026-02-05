package com.fullstack.ecommerce.service;

import com.fullstack.ecommerce.dto.OrderDto;
import com.fullstack.ecommerce.dto.OrderItemDto;
import com.fullstack.ecommerce.entity.*;
import com.fullstack.ecommerce.exception.BadRequestException;
import com.fullstack.ecommerce.exception.ResourceNotFoundException;
import com.fullstack.ecommerce.repository.*;
import com.fullstack.ecommerce.security.UserPrincipal;
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
 * Service class managing order operations for the e-commerce platform.
 * 
 * <p>This service provides comprehensive order management functionality including
 * order creation from cart items, order retrieval, and order history. It handles
 * the complete order lifecycle from cart checkout to order fulfillment.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Cart-to-order conversion with stock validation</li>
 *   <li>Automatic stock deduction upon order confirmation</li>
 *   <li>Order total calculation based on item prices and quantities</li>
 *   <li>User-specific order retrieval and history</li>
 *   <li>Pagination support for order listings</li>
 *   <li>Transaction management ensuring atomic order creation</li>
 *   <li>Authorization checks to prevent order access violations</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Orders are created from cart items during checkout</li>
 *   <li>Stock is validated and deducted atomically during order creation</li>
 *   <li>If any item has insufficient stock, the entire order is rolled back</li>
 *   <li>Cart is automatically cleared after successful order creation</li>
 *   <li>Order totals are calculated as sum of (unit price × quantity) for all items</li>
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
    
    /** Repository for cart item operations (used during checkout) */
    private final CartItemRepository cartItemRepository;
    
    /** Repository for user data retrieval */
    private final UserRepository userRepository;
    
    /** Repository for product data and stock management */
    private final ProductRepository productRepository;

    /**
     * Processes checkout by converting cart items into a confirmed order.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates that the cart is not empty</li>
     *   <li>Creates a new order with CONFIRMED status</li>
     *   <li>Validates stock availability for each cart item</li>
     *   <li>Deducts stock quantities from products</li>
     *   <li>Creates order items with product details and prices</li>
     *   <li>Calculates and sets the total order amount</li>
     *   <li>Clears the user's cart after successful order creation</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional. If any
     * product has insufficient stock, the entire order creation is rolled back,
     * ensuring data consistency.</p>
     * 
     * <p><b>Stock Management:</b> Stock is validated and deducted atomically.
     * If any item fails stock validation, the order is deleted and an exception
     * is thrown, preventing partial orders.</p>
     * 
     * @return {@link OrderDto} containing the created order details with all items and total
     * @throws BadRequestException if the cart is empty or if any product has insufficient stock
     * @throws ResourceNotFoundException if the user does not exist
     * 
     * @see OrderDto
     */
    @Transactional
    public OrderDto checkout() {
        // Get current authenticated user ID
        Long userId = getCurrentUserId();
        
        // Retrieve user entity - required for order association
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        // Retrieve all cart items for the user
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        
        // Validate cart is not empty
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        
        // Initialize order total and items list
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        // Create order with CONFIRMED status (initial total will be updated after items are processed)
        Order order = Order.builder().user(user).status(OrderStatus.CONFIRMED).totalAmount(BigDecimal.ZERO).build();
        order = orderRepository.save(order);
        
        // Process each cart item: validate stock, deduct inventory, create order item
        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            
            // Validate stock availability - if insufficient, rollback entire order
            if (p.getStock() < ci.getQuantity()) {
                orderRepository.delete(order);
                throw new BadRequestException("Insufficient stock for: " + p.getName());
            }
            
            // Deduct stock from product inventory
            p.setStock(p.getStock() - ci.getQuantity());
            productRepository.save(p);
            
            // Create order item with product details and snapshot of price at time of order
            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(ci.getQuantity())
                    .unitPrice(p.getPrice()) // Store price snapshot for order history
                    .build();
            oi = orderItemRepository.save(oi);
            orderItems.add(oi);
            
            // Accumulate total: unit price × quantity
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        
        // Update order with calculated total amount
        order.setTotalAmount(total);
        orderRepository.save(order);
        
        // Clear user's cart after successful order creation
        cartItemRepository.deleteByUserId(userId);
        
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
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
    public OrderDto getOrderById(Long id) {
        // Retrieve order and validate existence
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
        
        // Authorization check - ensure user owns this order
        if (!order.getUser().getId().equals(getCurrentUserId())) {
            throw new BadRequestException("Not your order");
        }
        
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
     * associated order items with calculated subtotals.</p>
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
                    .productId(oi.getProduct().getId())
                    .productName(oi.getProduct().getName())
                    .quantity(oi.getQuantity())
                    .unitPrice(oi.getUnitPrice())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());
        
        // Build and return order DTO with all details
        return OrderDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(itemDtos)
                .build();
    }
}

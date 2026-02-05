package com.fullstack.eventbooking.service;

import com.fullstack.eventbooking.dto.BookingDto;
import com.fullstack.eventbooking.entity.Booking;
import com.fullstack.eventbooking.entity.Event;
import com.fullstack.eventbooking.entity.User;
import com.fullstack.eventbooking.exception.BadRequestException;
import com.fullstack.eventbooking.exception.ResourceNotFoundException;
import com.fullstack.eventbooking.repository.BookingRepository;
import com.fullstack.eventbooking.repository.EventRepository;
import com.fullstack.eventbooking.repository.UserRepository;
import com.fullstack.eventbooking.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing event booking operations for the event booking platform.
 * 
 * <p>This service provides comprehensive booking management functionality including
 * creating bookings, retrieving booking history, and managing booking confirmations.
 * It handles the complete booking lifecycle from seat reservation to confirmation.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Event booking creation with seat availability validation</li>
 *   <li>Automatic seat deduction upon booking confirmation</li>
 *   <li>User-specific booking retrieval and history</li>
 *   <li>Booking confirmation email notifications</li>
 *   <li>Pagination support for booking listings</li>
 *   <li>Transaction management ensuring atomic booking creation</li>
 *   <li>Authorization checks to prevent booking access violations</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Bookings are created with seat availability validation</li>
 *   <li>Available seats are automatically deducted upon successful booking</li>
 *   <li>If insufficient seats are available, booking is rejected</li>
 *   <li>Booking confirmation emails are sent automatically after successful booking</li>
 *   <li>Bookings are user-specific and cannot be accessed by other users</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    /** Repository for booking persistence operations */
    private final BookingRepository bookingRepository;
    
    /** Repository for event data and seat availability management */
    private final EventRepository eventRepository;
    
    /** Repository for user data retrieval */
    private final UserRepository userRepository;
    
    /** Service for sending booking confirmation emails */
    private final MockEmailService mockEmailService;

    /**
     * Creates a new booking for an event with the specified number of seats.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates event existence</li>
     *   <li>Validates seat availability against requested quantity</li>
     *   <li>Creates a new booking with unique booking ID</li>
     *   <li>Deducts available seats from the event</li>
     *   <li>Sends booking confirmation email to the user</li>
     *   <li>Returns booking details</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that booking creation and seat deduction are atomic. If email sending fails,
     * the booking is still created but the error is logged.</p>
     * 
     * <p><b>Seat Management:</b> Available seats are validated and deducted atomically.
     * If insufficient seats are available, the booking is rejected with a BadRequestException.</p>
     * 
     * @param eventId the unique identifier of the event to book
     * @param seats the number of seats to book (must be positive and not exceed available seats)
     * @return {@link BookingDto} containing the created booking details including booking ID
     * @throws BadRequestException if insufficient seats are available
     * @throws ResourceNotFoundException if the event or user does not exist
     * 
     * @see BookingDto
     */
    @Transactional
    public BookingDto book(Long eventId, Integer seats) {
        // Get current authenticated user ID
        Long userId = getCurrentUserId();
        
        // Retrieve user entity - required for booking association
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        // Retrieve event entity and validate existence
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", eventId));
        
        // Validate seat availability
        if (event.getAvailableSeats() < seats) {
            throw new BadRequestException("Not enough seats available");
        }
        
        // Create booking with unique booking ID (generated by entity)
        Booking booking = Booking.builder().user(user).event(event).seats(seats).build();
        booking = bookingRepository.save(booking);
        
        // Deduct available seats from event
        event.setAvailableSeats(event.getAvailableSeats() - seats);
        eventRepository.save(event);
        
        // Send booking confirmation email
        mockEmailService.sendBookingConfirmation(user.getEmail(), user.getName(), booking.getBookingId(), event.getName(), seats);
        
        return toDto(booking);
    }

    /**
     * Retrieves paginated booking history for the currently authenticated user.
     * 
     * <p>This method fetches bookings belonging to the logged-in user, sorted by
     * creation date in descending order (most recent first). Results are paginated
     * to support efficient retrieval of large booking histories.</p>
     * 
     * @param page the page number (0-indexed)
     * @param size the number of bookings per page
     * @return list of {@link BookingDto} objects representing user's bookings,
     *         sorted by creation date descending
     * 
     * @see BookingDto
     */
    public List<BookingDto> getMyBookings(int page, int size) {
        Long userId = getCurrentUserId();
        
        // Create pagination request sorted by creation date (newest first)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        // Fetch paginated bookings and convert to DTOs
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific booking by its unique booking ID.
     * 
     * <p>This method fetches booking details using the human-readable booking ID
     * (e.g., "BK-2024-001234"). Only the owner of the booking can access it,
     * ensuring data privacy and security.</p>
     * 
     * <p><b>Authorization:</b> Only the user who made the booking can retrieve it.
     * Attempts to access other users' bookings will result in a BadRequestException.</p>
     * 
     * @param bookingId the unique booking identifier (e.g., "BK-2024-001234")
     * @return {@link BookingDto} containing complete booking details
     * @throws ResourceNotFoundException if the booking with the given ID does not exist
     * @throws BadRequestException if the booking does not belong to the current user
     * 
     * @see BookingDto
     */
    public BookingDto getByBookingId(String bookingId) {
        // Retrieve booking by booking ID and validate existence
        Booking b = bookingRepository.findByBookingId(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        
        // Authorization check - ensure user owns this booking
        if (!b.getUser().getId().equals(getCurrentUserId())) throw new BadRequestException("Not your booking");
        
        return toDto(b);
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
     * Converts a Booking entity to its corresponding DTO.
     * 
     * <p>This helper method transforms the entity to a DTO for client consumption,
     * including event details and booking information.</p>
     * 
     * @param b the booking entity to convert
     * @return {@link BookingDto} containing all booking details
     */
    private BookingDto toDto(Booking b) {
        // Build and return DTO with all booking details
        return BookingDto.builder()
                .id(b.getId())
                .bookingId(b.getBookingId())
                .eventId(b.getEvent().getId())
                .eventName(b.getEvent().getName())
                .seats(b.getSeats())
                .createdAt(b.getCreatedAt())
                .build();
    }
}

package com.fullstack.eventbooking.service;

import com.fullstack.eventbooking.dto.EventDto;
import com.fullstack.eventbooking.entity.Event;
import com.fullstack.eventbooking.exception.BadRequestException;
import com.fullstack.eventbooking.exception.ResourceNotFoundException;
import com.fullstack.eventbooking.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing event operations for the event booking platform.
 * 
 * <p>This service provides comprehensive event management functionality including
 * creating, reading, updating, and deleting events. Events represent activities
 * that users can book seats for, such as concerts, conferences, or workshops.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Complete CRUD operations for events</li>
 *   <li>Event listing and retrieval by ID</li>
 *   <li>Event creation with venue, date, and seat capacity</li>
 *   <li>Event updates with seat capacity validation</li>
 *   <li>Automatic available seat tracking</li>
 *   <li>Event deletion with existence validation</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Events track total seats and available seats separately</li>
 *   <li>Available seats are automatically updated when bookings are created</li>
 *   <li>Total seats cannot be reduced below already booked seats</li>
 *   <li>All event operations are transactional to ensure data integrity</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class EventService {

    /** Repository for event persistence operations */
    private final EventRepository eventRepository;

    /**
     * Retrieves all events in the system.
     * 
     * <p>This method fetches all available events and converts them to DTOs
     * for client consumption. Useful for populating event listings and
     * browsing pages.</p>
     * 
     * @return list of {@link EventDto} objects representing all events
     * 
     * @see EventDto
     */
    public List<EventDto> findAll() {
        return eventRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific event by its unique identifier.
     * 
     * @param id the unique identifier of the event to retrieve
     * @return {@link EventDto} containing the event details including seat availability
     * @throws ResourceNotFoundException if the event with the given ID does not exist
     * 
     * @see EventDto
     */
    public EventDto getById(Long id) {
        Event e = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", id));
        return toDto(e);
    }

    /**
     * Creates a new event in the system.
     * 
     * <p>This method creates a new event with the provided details. The initial
     * available seats are set equal to total seats, as no bookings exist yet.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that event creation is atomic and consistent.</p>
     * 
     * @param name the name of the event (required)
     * @param description the description of the event
     * @param venue the venue where the event will take place
     * @param eventDate the date and time when the event will occur
     * @param totalSeats the total number of seats available for the event
     * @return {@link EventDto} containing the created event details including generated ID
     * 
     * @see EventDto
     */
    @Transactional
    public EventDto create(String name, String description, String venue, Instant eventDate, Integer totalSeats) {
        // Build event entity with initial available seats equal to total seats
        Event e = Event.builder()
                .name(name)
                .description(description)
                .venue(venue)
                .eventDate(eventDate)
                .totalSeats(totalSeats)
                .availableSeats(totalSeats) // Initially all seats are available
                .build();
        
        // Persist event to database
        e = eventRepository.save(e);
        
        // Convert to DTO and return
        return toDto(e);
    }

    /**
     * Updates an existing event's details.
     * 
     * <p>This method allows modification of event information. When updating total seats,
     * the method validates that the new total is not less than already booked seats.
     * Available seats are automatically recalculated based on the difference.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that event updates are atomic and consistent.</p>
     * 
     * <p><b>Seat Management:</b> If total seats are updated, the system calculates
     * the number of booked seats and ensures the new total is sufficient. Available
     * seats are then set to the difference between total and booked seats.</p>
     * 
     * @param id the unique identifier of the event to update
     * @param name the new name for the event
     * @param description the new description for the event
     * @param venue the new venue for the event
     * @param eventDate the new date and time for the event
     * @param totalSeats the new total number of seats (optional, existing value preserved if null)
     * @return {@link EventDto} containing the updated event details
     * @throws ResourceNotFoundException if the event with the given ID does not exist
     * @throws BadRequestException if attempting to set total seats less than already booked seats
     * 
     * @see EventDto
     */
    @Transactional
    public EventDto update(Long id, String name, String description, String venue, Instant eventDate, Integer totalSeats) {
        // Retrieve event and validate existence
        Event e = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", id));
        
        // Update basic event properties
        e.setName(name);
        e.setDescription(description);
        e.setVenue(venue);
        e.setEventDate(eventDate);
        
        // Handle total seats update with validation
        if (totalSeats != null) {
            // Calculate number of already booked seats
            int booked = e.getTotalSeats() - e.getAvailableSeats();
            
            // Validate new total is not less than booked seats
            if (totalSeats < booked) throw new BadRequestException("Cannot set total seats less than already booked");
            
            // Update total seats and recalculate available seats
            e.setTotalSeats(totalSeats);
            e.setAvailableSeats(totalSeats - booked);
        }
        
        // Persist changes
        e = eventRepository.save(e);
        
        // Convert to DTO and return
        return toDto(e);
    }

    /**
     * Deletes an event from the system.
     * 
     * <p>This method permanently removes an event after validating its existence.
     * The operation is transactional to ensure data consistency.</p>
     * 
     * <p><b>Note:</b> Consider the impact on existing bookings before deleting
     * an event. Events with active bookings should typically be cancelled or marked
     * as inactive rather than hard-deleted.</p>
     * 
     * @param id the unique identifier of the event to delete
     * @throws ResourceNotFoundException if the event with the given ID does not exist
     */
    @Transactional
    public void delete(Long id) {
        // Validate event existence before deletion
        if (!eventRepository.existsById(id)) throw new ResourceNotFoundException("Event", id);
        
        // Permanently delete the event
        eventRepository.deleteById(id);
    }

    /**
     * Converts an Event entity to its corresponding DTO.
     * 
     * <p>This helper method transforms the entity to a DTO for client consumption,
     * including all event details and seat availability information.</p>
     * 
     * @param e the event entity to convert
     * @return {@link EventDto} containing all event details
     */
    private EventDto toDto(Event e) {
        // Build and return DTO with all event details
        return EventDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .venue(e.getVenue())
                .eventDate(e.getEventDate())
                .totalSeats(e.getTotalSeats())
                .availableSeats(e.getAvailableSeats())
                .build();
    }
}

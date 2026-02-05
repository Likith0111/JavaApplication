package com.fullstack.eventbooking.mapper;

import com.fullstack.eventbooking.dto.BookingDto;
import com.fullstack.eventbooking.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        if (booking == null) return null;
        
        BookingDto.BookingDtoBuilder builder = BookingDto.builder()
                .id(booking.getId())
                .bookingId(booking.getBookingId())
                .seats(booking.getSeats())
                .createdAt(booking.getCreatedAt());
        
        if (booking.getEvent() != null) {
            builder.eventId(booking.getEvent().getId())
                   .eventName(booking.getEvent().getName());
        }
        
        return builder.build();
    }

    public Booking toEntity(BookingDto dto) {
        if (dto == null) return null;
        
        return Booking.builder()
                .id(dto.getId())
                .bookingId(dto.getBookingId())
                .seats(dto.getSeats())
                .build();
    }
}

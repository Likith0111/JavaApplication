package com.fullstack.eventbooking.mapper;

import com.fullstack.eventbooking.dto.EventDto;
import com.fullstack.eventbooking.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDto toDto(Event event) {
        if (event == null) return null;
        
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .venue(event.getVenue())
                .eventDate(event.getEventDate())
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .build();
    }

    public Event toEntity(EventDto dto) {
        if (dto == null) return null;
        
        return Event.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .venue(dto.getVenue())
                .eventDate(dto.getEventDate())
                .totalSeats(dto.getTotalSeats())
                .availableSeats(dto.getAvailableSeats())
                .build();
    }
}

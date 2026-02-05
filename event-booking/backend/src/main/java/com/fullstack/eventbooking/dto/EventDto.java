package com.fullstack.eventbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private Long id;
    private String name;
    private String description;
    private String venue;
    private Instant eventDate;
    private Integer totalSeats;
    private Integer availableSeats;
}

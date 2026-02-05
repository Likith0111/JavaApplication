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
public class BookingDto {

    private Long id;
    private String bookingId;
    private Long eventId;
    private String eventName;
    private Integer seats;
    private Instant createdAt;
}

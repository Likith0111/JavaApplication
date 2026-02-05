package com.fullstack.eventbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotBlank
    @Size(max = 200)
    private String venue;

    @NotNull
    @Column(nullable = false)
    private Instant eventDate;

    @NotNull
    @Column(nullable = false)
    private Integer totalSeats;

    @NotNull
    @Column(nullable = false)
    private Integer availableSeats;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (availableSeats == null) availableSeats = totalSeats;
    }
}

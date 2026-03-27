package ru.restaurant.booking_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private Long customerId;
    private Long restaurantId;
    private Long tableId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfGuests;
    private String specialRequests;
    private ReservationStatus status;
    private List<MenuItem> preOrderedItems = new ArrayList<>();

    public Reservation(Long id, Long customerId, Long restaurantId, Long tableId,
                       LocalDateTime startTime, int numberOfGuests) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = startTime.plusHours(2); // стандартное время брони - 2 часа
        this.numberOfGuests = numberOfGuests;
        this.status = ReservationStatus.PENDING;
    }
}
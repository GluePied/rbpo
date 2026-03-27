package ru.restaurant.booking_system.repository;

import org.springframework.stereotype.Repository;
import ru.restaurant.booking_system.model.Reservation;
import ru.restaurant.booking_system.model.ReservationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {
    private final ConcurrentHashMap<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findByCustomerId(Long customerId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByRestaurantId(Long restaurantId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByTableId(Long tableId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getTableId().equals(tableId))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByDate(LocalDateTime date) {
        return reservations.values().stream()
                .filter(reservation ->
                        reservation.getStartTime().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Reservation> findActiveReservations() {
        return reservations.values().stream()
                .filter(reservation ->
                        reservation.getStatus() == ReservationStatus.CONFIRMED &&
                                reservation.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(idGenerator.getAndIncrement());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public void deleteById(Long id) {
        reservations.remove(id);
    }

    public boolean existsById(Long id) {
        return reservations.containsKey(id);
    }

    public boolean hasOverlappingReservation(Long tableId, LocalDateTime start, LocalDateTime end) {
        return reservations.values().stream()
                .filter(r -> r.getTableId().equals(tableId))
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .anyMatch(r -> timesOverlap(r.getStartTime(), r.getEndTime(), start, end));
    }

    private boolean timesOverlap(LocalDateTime start1, LocalDateTime end1,
                                 LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
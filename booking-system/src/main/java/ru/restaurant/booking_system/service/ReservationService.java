package ru.restaurant.booking_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.restaurant.booking_system.model.Reservation;
import ru.restaurant.booking_system.model.ReservationStatus;
import ru.restaurant.booking_system.repository.ReservationRepository;
import ru.restaurant.booking_system.repository.TableRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    public Reservation createReservation(Reservation reservation) {
        // Проверяем, что стол существует
        if (!tableRepository.existsById(reservation.getTableId())) {
            throw new IllegalArgumentException("Table not found");
        }

        // Проверяем, что время бронирования не в прошлом
        if (reservation.getStartTime() == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }

        if (reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book in the past");
        }

        // Если endTime не задан, устанавливаем через 2 часа
        if (reservation.getEndTime() == null) {
            reservation.setEndTime(reservation.getStartTime().plusHours(2));
        }

        // Проверяем, что стол свободен на это время
        if (!isTableAvailable(reservation.getTableId(),
                reservation.getStartTime(),
                reservation.getEndTime())) {
            throw new IllegalArgumentException("Table is already booked for this time");
        }

        return reservationRepository.save(reservation);
    }

    public boolean isTableAvailable(Long tableId, LocalDateTime start, LocalDateTime end) {
        // Защита от null
        if (tableId == null) {
            throw new IllegalArgumentException("Table ID cannot be null");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (end == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }

        // Проверяем, что время начала не позже времени окончания
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Проверяем, что время не в прошлом
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot check availability for past time");
        }

        List<Reservation> reservations = reservationRepository.findByTableId(tableId);

        return reservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .noneMatch(r -> timesOverlap(r.getStartTime(), r.getEndTime(), start, end));
    }

    private boolean timesOverlap(LocalDateTime start1, LocalDateTime end1,
                                 LocalDateTime start2, LocalDateTime end2) {
        // Защита от null
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false; // Если любое время null, считаем что пересечения нет
        }
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    public List<Reservation> findByRestaurantId(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    public List<Reservation> findByDate(LocalDateTime date) {
        return reservationRepository.findByDate(date);
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        reservation.setId(id);

        // Проверка времени
        if (reservation.getStartTime() != null && reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot update to past time");
        }

        return reservationRepository.save(reservation);
    }

    public Reservation updateStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
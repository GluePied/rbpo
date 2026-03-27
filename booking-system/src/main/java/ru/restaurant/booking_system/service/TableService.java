package ru.restaurant.booking_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.restaurant.booking_system.model.Table;
import ru.restaurant.booking_system.model.TableStatus;
import ru.restaurant.booking_system.repository.RestaurantRepository;
import ru.restaurant.booking_system.repository.TableRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    public Table create(Table table) {
        // Проверяем, что ресторан существует
        if (!restaurantRepository.existsById(table.getRestaurantId())) {
            throw new IllegalArgumentException("Restaurant not found with id: " + table.getRestaurantId());
        }

        // Проверяем, что номер столика уникален в ресторане
        List<Table> existingTables = tableRepository.findByRestaurantId(table.getRestaurantId());
        boolean tableNumberExists = existingTables.stream()
                .anyMatch(t -> t.getTableNumber() == table.getTableNumber());

        if (tableNumberExists) {
            throw new IllegalArgumentException("Table number " + table.getTableNumber() +
                    " already exists in this restaurant");
        }

        // Устанавливаем начальный статус
        if (table.getStatus() == null) {
            table.setStatus(TableStatus.AVAILABLE);
        }

        return tableRepository.save(table);
    }

    public List<Table> findAll() {
        return tableRepository.findAll();
    }

    public Optional<Table> findById(Long id) {
        return tableRepository.findById(id);
    }

    public List<Table> findByRestaurantId(Long restaurantId) {
        return tableRepository.findByRestaurantId(restaurantId);
    }

    public List<Table> findAvailableTables(Long restaurantId, int minCapacity) {
        return tableRepository.findByRestaurantIdAndStatus(restaurantId, TableStatus.AVAILABLE)
                .stream()
                .filter(table -> table.getCapacity() >= minCapacity)
                .toList();
    }

    public Table update(Long id, Table table) {
        if (!tableRepository.existsById(id)) {
            throw new IllegalArgumentException("Table not found with id: " + id);
        }
        table.setId(id);
        return tableRepository.save(table);
    }

    public Table updateStatus(Long id, TableStatus status) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Table not found with id: " + id));
        table.setStatus(status);
        return tableRepository.save(table);
    }

    public void deleteById(Long id) {
        // Здесь можно добавить проверку, есть ли у столика активные брони
        tableRepository.deleteById(id);
    }
}
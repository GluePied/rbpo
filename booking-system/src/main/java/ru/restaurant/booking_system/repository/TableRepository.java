package ru.restaurant.booking_system.repository;

import org.springframework.stereotype.Repository;
import ru.restaurant.booking_system.model.Table;
import ru.restaurant.booking_system.model.TableStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TableRepository {
    private final ConcurrentHashMap<Long, Table> tables = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Table> findAll() {
        return new ArrayList<>(tables.values());
    }

    public Optional<Table> findById(Long id) {
        return Optional.ofNullable(tables.get(id));
    }

    public List<Table> findByRestaurantId(Long restaurantId) {
        return tables.values().stream()
                .filter(table -> table.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    public List<Table> findByRestaurantIdAndStatus(Long restaurantId, TableStatus status) {
        return tables.values().stream()
                .filter(table -> table.getRestaurantId().equals(restaurantId))
                .filter(table -> table.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Table> findByCapacity(int minCapacity) {
        return tables.values().stream()
                .filter(table -> table.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    public Table save(Table table) {
        if (table.getId() == null) {
            table.setId(idGenerator.getAndIncrement());
        }
        tables.put(table.getId(), table);
        return table;
    }

    public void deleteById(Long id) {
        tables.remove(id);
    }

    public boolean existsById(Long id) {
        return tables.containsKey(id);
    }

    public void updateStatus(Long id, TableStatus status) {
        Table table = tables.get(id);
        if (table != null) {
            table.setStatus(status);
        }
    }
}
package ru.restaurant.booking_system.repository;

import org.springframework.stereotype.Repository;
import ru.restaurant.booking_system.model.MenuItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MenuItemRepository {
    private final ConcurrentHashMap<Long, MenuItem> menuItems = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<MenuItem> findAll() {
        return new ArrayList<>(menuItems.values());
    }

    public Optional<MenuItem> findById(Long id) {
        return Optional.ofNullable(menuItems.get(id));
    }

    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return menuItems.values().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    public List<MenuItem> findByRestaurantIdAndCategory(Long restaurantId, String category) {
        return menuItems.values().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<MenuItem> findByRestaurantIdAndAvailable(Long restaurantId, boolean available) {
        return menuItems.values().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
                .filter(item -> item.isAvailable() == available)
                .collect(Collectors.toList());
    }

    public List<MenuItem> findByPriceRange(BigDecimal min, BigDecimal max) {
        return menuItems.values().stream()
                .filter(item -> item.getPrice().compareTo(min) >= 0)
                .filter(item -> item.getPrice().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public MenuItem save(MenuItem menuItem) {
        if (menuItem.getId() == null) {
            menuItem.setId(idGenerator.getAndIncrement());
        }
        menuItems.put(menuItem.getId(), menuItem);
        return menuItem;
    }

    public void deleteById(Long id) {
        menuItems.remove(id);
    }

    public boolean existsById(Long id) {
        return menuItems.containsKey(id);
    }

    public void updateAvailability(Long id, boolean available) {
        MenuItem item = menuItems.get(id);
        if (item != null) {
            item.setAvailable(available);
        }
    }

    public List<MenuItem> findByIds(List<Long> ids) {
        return ids.stream()
                .map(menuItems::get)
                .filter(item -> item != null)
                .collect(Collectors.toList());
    }
}
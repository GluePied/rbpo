package ru.restaurant.booking_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.restaurant.booking_system.model.MenuItem;
import ru.restaurant.booking_system.repository.MenuItemRepository;
import ru.restaurant.booking_system.repository.RestaurantRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItem create(MenuItem menuItem) {
        // Проверяем, что ресторан существует
        if (!restaurantRepository.existsById(menuItem.getRestaurantId())) {
            throw new IllegalArgumentException("Restaurant not found with id: " + menuItem.getRestaurantId());
        }

        // Валидация цены
        if (menuItem.getPrice() == null || menuItem.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        // Валидация названия
        if (menuItem.getName() == null || menuItem.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be empty");
        }

        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public List<MenuItem> findByRestaurantIdAndCategory(Long restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category);
    }

    public List<MenuItem> findByRestaurantIdAndAvailable(Long restaurantId, boolean available) {
        return menuItemRepository.findByRestaurantIdAndAvailable(restaurantId, available);
    }

    public List<MenuItem> findByPriceRange(BigDecimal min, BigDecimal max) {
        return menuItemRepository.findByPriceRange(min, max);
    }

    public MenuItem update(Long id, MenuItem menuItem) {
        if (!menuItemRepository.existsById(id)) {
            throw new IllegalArgumentException("Menu item not found with id: " + id);
        }
        menuItem.setId(id);
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateAvailability(Long id, boolean available) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));
        menuItem.setAvailable(available);
        return menuItemRepository.save(menuItem);
    }

    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> findByIds(List<Long> ids) {
        return menuItemRepository.findByIds(ids);
    }
}
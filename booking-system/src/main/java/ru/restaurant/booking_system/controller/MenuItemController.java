package ru.restaurant.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restaurant.booking_system.model.MenuItem;
import ru.restaurant.booking_system.service.MenuItemService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        try {
            MenuItem created = menuItemService.create(menuItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return menuItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.findByRestaurantId(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuByCategory(
            @PathVariable Long restaurantId,
            @PathVariable String category) {
        return ResponseEntity.ok(menuItemService.findByRestaurantIdAndCategory(restaurantId, category));
    }

    @GetMapping("/restaurant/{restaurantId}/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenu(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "true") boolean available) {
        return ResponseEntity.ok(menuItemService.findByRestaurantIdAndAvailable(restaurantId, available));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItem>> getMenuByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(menuItemService.findByPriceRange(min, max));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody MenuItem menuItem) {
        try {
            MenuItem updated = menuItemService.update(id, menuItem);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<MenuItem> updateMenuItemAvailability(@PathVariable Long id,
                                                               @RequestParam boolean available) {
        try {
            MenuItem updated = menuItemService.updateAvailability(id, available);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
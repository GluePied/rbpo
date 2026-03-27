package ru.restaurant.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restaurant.booking_system.model.Restaurant;
import ru.restaurant.booking_system.model.CuisineType;
import ru.restaurant.booking_system.service.RestaurantService;
import ru.restaurant.booking_system.service.TableService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final TableService tableService;

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            Restaurant created = restaurantService.create(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cuisine/{cuisineType}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCuisine(@PathVariable CuisineType cuisineType) {
        return ResponseEntity.ok(restaurantService.findByCuisineType(cuisineType));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(restaurantService.findByNameContaining(name));
        } else if (address != null && !address.isEmpty()) {
            return ResponseEntity.ok(restaurantService.findByAddressContaining(address));
        }
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/with-tables")
    public ResponseEntity<List<Restaurant>> getRestaurantsWithMinTables(@RequestParam int minTables) {
        return ResponseEntity.ok(restaurantService.findByMinTablesCount(minTables));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestBody Restaurant restaurant) {
        try {
            Restaurant updated = restaurantService.update(id, restaurant);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/phone")
    public ResponseEntity<Restaurant> updateRestaurantPhone(@PathVariable Long id,
                                                            @RequestParam String phone) {
        try {
            Restaurant updated = restaurantService.updatePhone(id, phone);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<RestaurantStatistics> getRestaurantStatistics(@PathVariable Long id) {
        if (!restaurantService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        RestaurantStatistics stats = new RestaurantStatistics();
        stats.setRestaurantId(id);
        stats.setTotalTables(tableService.findByRestaurantId(id).size());
        stats.setAvailableTables(tableService.findAvailableTables(id, 1).size());

        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public static class RestaurantStatistics {
        private Long restaurantId;
        private int totalTables;
        private int availableTables;
        private int todayReservations;
        private double occupancyRate;

        // Геттеры и сеттеры
        public Long getRestaurantId() { return restaurantId; }
        public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
        public int getTotalTables() { return totalTables; }
        public void setTotalTables(int totalTables) { this.totalTables = totalTables; }
        public int getAvailableTables() { return availableTables; }
        public void setAvailableTables(int availableTables) { this.availableTables = availableTables; }
        public int getTodayReservations() { return todayReservations; }
        public void setTodayReservations(int todayReservations) { this.todayReservations = todayReservations; }
        public double getOccupancyRate() { return occupancyRate; }
        public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }
    }
}
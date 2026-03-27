package ru.restaurant.booking_system.repository;

import org.springframework.stereotype.Repository;
import ru.restaurant.booking_system.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RestaurantRepository {
    private final ConcurrentHashMap<Long, Restaurant> restaurants = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }

    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(restaurants.get(id));
    }

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idGenerator.getAndIncrement());
        }
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    public void deleteById(Long id) {
        restaurants.remove(id);
    }

    public boolean existsById(Long id) {
        return restaurants.containsKey(id);
    }
}
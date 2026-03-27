package ru.restaurant.booking_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.restaurant.booking_system.model.CuisineType;
import ru.restaurant.booking_system.model.Restaurant;
import ru.restaurant.booking_system.repository.RestaurantRepository;
import ru.restaurant.booking_system.repository.TableRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;  // добавили зависимость

    public Restaurant create(Restaurant restaurant) {
        // Валидация
        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty");
        }
        if (restaurant.getAddress() == null || restaurant.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant address cannot be empty");
        }
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    // НОВЫЙ МЕТОД: поиск по типу кухни
    public List<Restaurant> findByCuisineType(CuisineType cuisineType) {
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> restaurant.getCuisineType() == cuisineType)
                .collect(Collectors.toList());
    }

    // НОВЫЙ МЕТОД: поиск по названию (частичное совпадение)
    public List<Restaurant> findByNameContaining(String name) {
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> restaurant.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // НОВЫЙ МЕТОД: поиск по адресу (частичное совпадение)
    public List<Restaurant> findByAddressContaining(String address) {
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> restaurant.getAddress().toLowerCase().contains(address.toLowerCase()))
                .collect(Collectors.toList());
    }

    // НОВЫЙ МЕТОД: поиск ресторанов с минимальным количеством столиков
    public List<Restaurant> findByMinTablesCount(int minTables) {
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> {
                    int tablesCount = tableRepository.findByRestaurantId(restaurant.getId()).size();
                    return tablesCount >= minTables;
                })
                .collect(Collectors.toList());
    }

    public Restaurant update(Long id, Restaurant restaurant) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant not found with id: " + id);
        }
        restaurant.setId(id);
        return restaurantRepository.save(restaurant);
    }

    // НОВЫЙ МЕТОД: обновление только телефона
    public Restaurant updatePhone(Long id, String phone) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + id));
        restaurant.setPhone(phone);
        return restaurantRepository.save(restaurant);
    }

    public void deleteById(Long id) {
        // Проверяем, есть ли у ресторана столики
        if (!tableRepository.findByRestaurantId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete restaurant with existing tables");
        }
        restaurantRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }
}
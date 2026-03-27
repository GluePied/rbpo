package ru.restaurant.booking_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private Long id;
    private Long restaurantId;
    private int tableNumber;
    private int capacity;
    private String location; // например, "у окна", "в центре зала", "на веранде"
    private TableStatus status;

    public Table(Long id, Long restaurantId, int tableNumber, int capacity, String location) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.location = location;
        this.status = TableStatus.AVAILABLE;
    }
}
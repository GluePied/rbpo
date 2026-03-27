package ru.restaurant.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restaurant.booking_system.model.Table;
import ru.restaurant.booking_system.model.TableStatus;
import ru.restaurant.booking_system.service.TableService;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<Table> createTable(@RequestBody Table table) {
        try {
            Table created = tableService.create(table);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Table>> getAllTables() {
        return ResponseEntity.ok(tableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table> getTableById(@PathVariable Long id) {
        return tableService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Table>> getTablesByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(tableService.findByRestaurantId(restaurantId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Table>> getAvailableTables(
            @RequestParam Long restaurantId,
            @RequestParam int guests) {
        return ResponseEntity.ok(tableService.findAvailableTables(restaurantId, guests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Table> updateTable(@PathVariable Long id,
                                             @RequestBody Table table) {
        try {
            Table updated = tableService.update(id, table);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Table> updateTableStatus(@PathVariable Long id,
                                                   @RequestParam TableStatus status) {
        try {
            Table updated = tableService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
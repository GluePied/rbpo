package ru.restaurant.booking_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private CuisineType cuisineType;
    private List<Table> tables = new ArrayList<>();

    public Restaurant(Long id, String name, String address, String phone, CuisineType cuisineType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.cuisineType = cuisineType;
    }
}
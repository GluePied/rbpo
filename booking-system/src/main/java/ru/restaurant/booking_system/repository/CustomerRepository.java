package ru.restaurant.booking_system.repository;

import org.springframework.stereotype.Repository;
import ru.restaurant.booking_system.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class CustomerRepository {
    private final ConcurrentHashMap<Long, Customer> customers = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Optional<Customer> findByPhone(String phone) {
        return customers.values().stream()
                .filter(customer -> customer.getPhone().equals(phone))
                .findFirst();
    }

    public Optional<Customer> findByEmail(String email) {
        return customers.values().stream()
                .filter(customer -> customer.getEmail().equals(email))
                .findFirst();
    }

    public List<Customer> findByLastName(String lastName) {
        return customers.values().stream()
                .filter(customer -> customer.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(idGenerator.getAndIncrement());
        }
        customers.put(customer.getId(), customer);
        return customer;
    }

    public void deleteById(Long id) {
        customers.remove(id);
    }

    public boolean existsById(Long id) {
        return customers.containsKey(id);
    }

    public void addLoyaltyPoints(Long id, int points) {
        Customer customer = customers.get(id);
        if (customer != null) {
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        }
    }
}
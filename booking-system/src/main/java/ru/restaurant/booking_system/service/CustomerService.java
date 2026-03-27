package ru.restaurant.booking_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.restaurant.booking_system.model.Customer;
import ru.restaurant.booking_system.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s-]{10,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Customer create(Customer customer) {
        // Валидация телефона
        if (customer.getPhone() == null || !PHONE_PATTERN.matcher(customer.getPhone()).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        // Валидация email
        if (customer.getEmail() != null && !EMAIL_PATTERN.matcher(customer.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Проверка уникальности телефона
        Optional<Customer> existingByPhone = customerRepository.findByPhone(customer.getPhone());
        if (existingByPhone.isPresent()) {
            throw new IllegalArgumentException("Customer with this phone already exists");
        }

        // Проверка уникальности email
        if (customer.getEmail() != null) {
            Optional<Customer> existingByEmail = customerRepository.findByEmail(customer.getEmail());
            if (existingByEmail.isPresent()) {
                throw new IllegalArgumentException("Customer with this email already exists");
            }
        }

        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public List<Customer> findByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    public Customer update(Long id, Customer customer) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found with id: " + id);
        }
        customer.setId(id);
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        // Здесь можно добавить проверку на наличие активных броней
        customerRepository.deleteById(id);
    }

    public Customer addLoyaltyPoints(Long id, int points) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        return customerRepository.save(customer);
    }
}
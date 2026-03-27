package ru.restaurant.booking_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.restaurant.booking_system.model.*;
import ru.restaurant.booking_system.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			RestaurantRepository restaurantRepository,
			TableRepository tableRepository,
			CustomerRepository customerRepository,
			MenuItemRepository menuItemRepository) {

		return args -> {
			// Создаем ресторан
			Restaurant restaurant = new Restaurant(null, "Итальянский дворик",
					"ул. Пушкина, 10", "+7 (999) 123-45-67", CuisineType.ITALIAN);
			restaurant = restaurantRepository.save(restaurant);

			// Создаем столики
			for (int i = 1; i <= 5; i++) {
				Table table = new Table(null, restaurant.getId(), i,
						i % 2 == 0 ? 4 : 2, i % 2 == 0 ? "у окна" : "в центре зала");
				tableRepository.save(table);
			}

			// Создаем клиента
			Customer customer = new Customer(null, "Иван", "Петров",
					"+7 (999) 888-77-66", "ivan@email.com");
			customerRepository.save(customer);

			// Создаем меню
			MenuItem pizza = new MenuItem(null, restaurant.getId(),
					"Маргарита", "Классическая пицца с томатами и моцареллой",
					new BigDecimal("550.00"), "Пицца");
			menuItemRepository.save(pizza);

			MenuItem pasta = new MenuItem(null, restaurant.getId(),
					"Карбонара", "Паста с беконом в сливочном соусе",
					new BigDecimal("480.00"), "Паста");
			menuItemRepository.save(pasta);

			System.out.println("Тестовые данные загружены");
			System.out.println("Ресторан: " + restaurant.getName() + " (ID: " + restaurant.getId() + ")");
			System.out.println("Клиент: " + customer.getFirstName() + " " + customer.getLastName() + " (ID: " + customer.getId() + ")");
		};
	}
}
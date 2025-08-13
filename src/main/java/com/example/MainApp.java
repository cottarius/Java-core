package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.service.UserService;
import com.example.service.ProductService;
import com.example.model.Product;

@SpringBootApplication(scanBasePackages = {"com.example.service", "com.example.repository", "com.example.model", "com.example.controller"})
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService, ProductService productService) {
        return (args) -> {
            System.out.println("=== Работа с пользователями ===");
            userService.createUser("dave");
            userService.getAllUsers().forEach(user ->
                    System.out.println(user.getId() + ": " + user.getUsername())
            );
            var user = userService.getUser(1L);
            if (user != null)
                System.out.println("Получен: " + user.getUsername());
            
            System.out.println("\n=== Работа с продуктами ===");

            Product product1 = productService.createProduct("ACC003", new java.math.BigDecimal("1500.00"), Product.ProductType.ACCOUNT, 1L);
            Product product2 = productService.createProduct("CARD003", new java.math.BigDecimal("300.00"), Product.ProductType.CARD, 1L);
            
            System.out.println("Создан продукт: " + product1);
            System.out.println("Создан продукт: " + product2);

            var userProducts = productService.getProductsByUserId(1L);
            System.out.println("Продукты пользователя 1:");
            userProducts.forEach(p -> System.out.println("  " + p));

            var foundProduct = productService.getProduct(product1.getId());
            if (foundProduct != null) {
                System.out.println("Найден продукт по ID: " + foundProduct);
            }
        };
    }
}

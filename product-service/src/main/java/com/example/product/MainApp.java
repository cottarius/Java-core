package com.example.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.product.service.UserService;
import com.example.product.service.ProductService;
import com.example.product.model.Product;

@SpringBootApplication(scanBasePackages = {"com.example.product.service", "com.example.product.repository", "com.example.product.model", "com.example.product.controller"})
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService, ProductService productService) {
        return (args) -> {
            System.out.println("=== Работа с пользователями ===");
            userService.createUser("Dave Johnson", "dave@example.com");
            userService.getAllUsers().forEach(user ->
                    System.out.println(user.getId() + ": " + user.getName() + " (" + user.getEmail() + ")")
            );
            var user = userService.getUser(1L);
            if (user != null)
                System.out.println("Получен: " + user.getName());
            
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

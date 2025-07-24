package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.service.UserService;

@SpringBootApplication(scanBasePackages = {"com.example.service", "com.example.repository", "com.example.model"})
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService) {
        return (args) -> {
            userService.createUser("dave");
            userService.getAllUsers().forEach(user ->
                    System.out.println(user.getId() + ": " + user.getUsername())
            );
            var user = userService.getUser(1L);
            if (user != null)
                System.out.println("Получен: " + user.getUsername());
            userService.deleteUser(2L);
        };
    }
}

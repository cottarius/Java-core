import config.AppConfig;
import model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.UserService;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);

        userService.createUser("alice");
        userService.createUser("bob");

        userService.getAllUsers().forEach(user ->
                System.out.println(user.getId() + ": " + user.getUsername())
        );

        User user = userService.getUser(1L);
        if (user != null)
            System.out.println("Получен: " + user.getUsername());

        userService.deleteUser(2L);

        context.close();
    }
}

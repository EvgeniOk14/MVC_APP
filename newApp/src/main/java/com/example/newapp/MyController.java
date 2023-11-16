package com.example.newapp;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyController {
    private final MyEntityRepository myEntityRepository;
    private UserService userService;

    @Autowired
    public MyController(MyEntityRepository myEntityRepository, UserService userService) {
        this.myEntityRepository = myEntityRepository;
        this.userService = userService;
    }

    @GetMapping("/getDataFromDatabase")
    public String getDataFromDatabase(Model model) {
        Iterable<MyEntity> entities = myEntityRepository.findAll();
        model.addAttribute("entities", entities);
        return "showData"; // Название Thymeleaf-шаблона для отображения данных
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute User user, Model model) {
        userService.signUpUser(user);
        model.addAttribute("resultMessage", "User signed up successfully!");
        return "redirect:/"; // Или возвращайтесь на вашу домашнюю страницу
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        User existingUser = userService.getUser(user);
        if (existingUser != null) {
            // Пользователь найден, выполните необходимые действия
            model.addAttribute("resultMessage", "Login successful!");
        } else {
            // Пользователь не найден или неверный пароль
            model.addAttribute("resultMessage", "Invalid username or password");
        }
        return "redirect:/"; // Или возвращайтесь на вашу домашнюю страницу
    }
}

package com.example.newapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import  org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

        @PostMapping("/showAllUsers")
        public String showAllUsers(Model model) {
                UserService userService = new UserService();
                userService.showAllUsers1();

                // Добавьте атрибуты модели, если это необходимо
                model.addAttribute("resultMessage", "Users shown successfully");

                // Возвращаем имя Thymeleaf-шаблона
                return "redirect:/"; // Можете изменить URL, если нужно перейти на другую страницу
        }

//    @GetMapping("/")
//    public String home(Model model) {
//        // Можете добавить атрибуты модели, если необходимо
//        return "index"; // Возвращаем имя Thymeleaf-шаблона (без расширения)
//    }
//
//    @PostMapping("/processRequest")
//    public String processRequest(Model model) {
//        // Обработка события, например, выполнение какой-то логики
//
//        // Передача данных в представление
//        model.addAttribute("resultMessage", "Request processed successfully");
//
//        // Возвращаем имя Thymeleaf-шаблона (без расширения)
//        return "index";
//
//        // Добавьте методы для обработки других запросов и отображения соответствующих представлений
//    }

@GetMapping("/")
public String home(Model model) {
        // Создаем объект MyForm и добавляем его в модель
        model.addAttribute("myForm", new MyForm());
        return "index"; // Возвращаем имя Thymeleaf-шаблона (без расширения)
        }

@PostMapping("/processRequest")
public String processRequest(@ModelAttribute MyForm myForm, Model model) {
        // Обработка события, например, выполнение какой-то логики
        String userInput = myForm.getUserInput();

        // Передача данных в представление
        model.addAttribute("resultMessage", "Received user input: " + userInput);

        // Возвращаем имя Thymeleaf-шаблона (без расширения)
        return "index";
        }
}

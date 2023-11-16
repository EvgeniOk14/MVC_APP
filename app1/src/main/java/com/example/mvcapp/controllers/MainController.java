package com.example.mvcapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MainController {
    @GetMapping("/")
    public String home() {
        return "dialog"; // предполагается, что у вас есть файл main.html или main.jsp
    }

    @GetMapping("/showDialog")
    public String showDialog(Model model) {
        model.addAttribute("message", "Привет, это ваше диалоговое окно!");
        return "dialog";
    }
}


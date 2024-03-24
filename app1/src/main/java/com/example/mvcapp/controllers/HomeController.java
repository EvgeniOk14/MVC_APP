package com.example.mvcapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
    @GetMapping("/home")
    public String home()
    {
        return "home";
    }
    @GetMapping("/home1")
    public  String home1()
    {
        return "home1";
    }

}

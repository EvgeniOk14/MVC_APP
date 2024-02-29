package com.example.daocradapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController
{
    @GetMapping("/favicon.ico")
    public String favicon()
    {
        return "forward:/static/icon/favicon.ico"; // путь к иконке
    }
}

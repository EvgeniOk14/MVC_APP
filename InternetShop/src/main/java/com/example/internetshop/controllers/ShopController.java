package com.example.internetshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController
{
    @GetMapping("/")
        public String showFirstPage()
            {
                return "internetshop/shop";
            }
}

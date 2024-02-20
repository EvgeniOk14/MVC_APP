package com.example.daocrud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SecurityController
{
    @GetMapping("/")
    public String Security()
    {
        return "/people/index";
    }

    @PostMapping("/login")
    public String login()
    {
        return "security/security";
    }

    @PostMapping("/logout")
    public String logout()
    {
        return "/security/security";
    }

}





//import jakarta.validation.GroupSequence;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//    @GetMapping("/login")
//    public String login(HttpServletRequest request, HttpServletResponse response)
//    {
//    // Проверяем, была ли успешная аутентификация
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    if (auth != null && auth.isAuthenticated())
//    {
//        // Если пользователь уже аутентифицирован, перенаправляем на страницу /people/index
//        return "redirect:/people/index";
//    }
//    else
//    {
//        // Если аутентификация не прошла успешно, оставляем пользователя на странице регистрации
//        return "security/security";
//    }
//}

//    @GetMapping("/login")
//    public String login() {
//        // Получаем объект HttpServletRequest из контекста
//        HttpServletRequest request = (HttpServletRequest) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//
//        // Проверяем, была ли успешная аутентификация
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated()) {
//            // Если пользователь уже аутентифицирован, перенаправляем на страницу /people/index
//            return "redirect:/people/index";
//        } else {
//            // Если аутентификация не прошла успешно, оставляем пользователя на странице регистрации
//            return "security/security";
//        }
//    }
package com.example.mvcapp.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Получите атрибуты ошибки из запроса и добавьте их в модель, если это необходимо
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            model.addAttribute("statusCode", Integer.valueOf(status.toString()));
        }
        return "error"; // Верните имя представления ошибки (error.html или как вы его назвали)
    }


    public String getErrorPath() {
        return "/error";
    }
}
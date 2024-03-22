package com.example.daocradapi.controllers;

import com.example.daocradapi.models.delivery.DeliveryForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeliveryController
{

    @GetMapping("/deliveryForm")
    public String showDeliveryForm(Model model)
    {
        model.addAttribute("deliveryForm", new DeliveryForm());
        return "delivery/delivery"; // Возвращает имя представления (HTML-шаблона)
    }

    @PostMapping("/delivery/submit")
    public String submitDelivery(@ModelAttribute("deliveryForm") DeliveryForm deliveryForm)
    {
        // Здесь можно добавить логику для сохранения информации о доставке в базу данных или отправки на сервер
        // Возвращаем имя представления для отображения после успешной отправки формы
        return "delivery_success"; // Представление для отображения страницы успешной доставки
    }
}


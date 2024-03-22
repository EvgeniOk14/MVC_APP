package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.delivery.DeliveryDAO;
import com.example.daocradapi.models.delivery.DeliveryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeliveryController
{
    @Autowired
    private DeliveryDAO deliveryDAO;

    @GetMapping("/deliveryForm")
    public String showDeliveryForm(Model model)
    {
        model.addAttribute("deliveryForm", new DeliveryForm());
        return "delivery/delivery"; // Возвращает имя представления (HTML-шаблона)
    }

    @PostMapping("/delivery/submit")
    public String submitDelivery(@ModelAttribute("recipientName") String recipientName,
                                 @ModelAttribute("address") String address,
                                 @ModelAttribute("city") String city,
                                 @ModelAttribute("postIndex") String postIndex,
                                 @ModelAttribute("country") String country,
                                 @ModelAttribute("phone") String phone,
                                 @ModelAttribute("deliveryForm") DeliveryForm deliveryForm,
                                 Model model)
    {
        deliveryForm.setRecipientName(recipientName);
        deliveryForm.setAddress(address);
        deliveryForm.setCity(city);
        deliveryForm.setPostIndex(postIndex);
        deliveryForm.setCountry(country);
        deliveryForm.setPhone(phone);

        deliveryDAO.saveDelivery(deliveryForm);

        model.addAttribute("deliveryForm", deliveryForm);
        model.addAttribute("recipientName", recipientName);
        model.addAttribute("city", city);
        model.addAttribute("postIndex", postIndex);
        model.addAttribute("country", country);
        model.addAttribute("phone", phone);

        return "delivery/deliverySuccess"; // Представление для отображения страницы успешной доставки
    }
}


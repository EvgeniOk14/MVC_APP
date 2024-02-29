package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.products.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class ThingController
{
    //region Fields
    @Autowired // внедрение поля спрингом (вместо конструктора)
    private ThingDAO thingDAO;
    //endregion

    /** Показ всего списка вещей из каталога (БД) **/
    @GetMapping("/things")
    public String getAllThigs(Model model)
    {
       List<Thing> things = thingDAO.getAllThigs();
        model.addAttribute("things", things);
        return "things/things";
    }

    /** переход на форму для добавление новой вещи в каталог (БД) **/
    @GetMapping("/showFormThing")
    public String formThings(Model model, NewThing newThing)
    {
        model.addAttribute("newthing", newThing);
        return "things/showFormThing";
    }

    /** добавление новой вещи в каталог (БД)  **/
    @PostMapping("/saveThingsFromForm")
    public String saveThing(@ModelAttribute("newthing") NewThing newThing)
    {
        thingDAO.saveThing(newThing); // сохранения новой вещи
        return "redirect:/things"; // Перенаправление пользователя на список вещей после сохранения
    }

    /** метод удаление вещи из каталога (БД) **/
    @GetMapping("/deleteThing/{thing_id}")
    public String deleteThing(@PathVariable("thing_id") Integer thing_id)
    {
        thingDAO.deleteThing(thing_id);
        return "redirect:/things";
    }

    /** переход на форму редактирования вещи **/
    @GetMapping("/edit/{thing_id}")
    public String editForm(Model model, @PathVariable("thing_id") Integer thing_id)
    {
        model.addAttribute("newThing", thingDAO.getThingById(thing_id));
        return "things/editThing";
    }

    /** редактирование вещи по номеру thing_id **/
    @PostMapping("/editThing/{thing_id}/edit")
    public String editThing(@ModelAttribute("newThing") NewThing newThing, @PathVariable("thing_id") Integer thing_id)
    {
        thingDAO.editThing(thing_id, newThing);
        return "redirect:/things";
    }
}

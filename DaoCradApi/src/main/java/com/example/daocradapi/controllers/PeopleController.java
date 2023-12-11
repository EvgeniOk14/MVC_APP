package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PeopleController
{
    //region Field
    private final PersonDAO personDAO;
    //endregion

    //region Constructor
    @Autowired
    public PeopleController(PersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }
    //endregion

    @GetMapping("shop/shop")
    public String showStartPage()
    {
        return "shop/shop";
    }

    @GetMapping("people/shop/start")
    public String showStartPage(Model model)
    {
        model.addAttribute("message", "Привет, мир!");
        return "people/shop/start";
    }

    /** показ всех людей! (Выполнение запроса: "SELECT * FROM person2") **/
    @GetMapping("/")
    public String index(Model model) // получим всех дюдей из DAO и передадим их на отображение в представление
    {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    /** показать человека по заданному id! (Выполнение запроса: "SELECT * FROM person2 WHERE id=?") **/
    @GetMapping("people/{id}")
    public String Show(@PathVariable("id") Integer id, Model model) // получим одного человека по id из DAO и передадим его на отображение в представление
    {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    /** создать нового человека **/
    @GetMapping("people/new")
    public String newPerson(@ModelAttribute("person") Person person)
    {
        return "people/new";
    }

    /** метод создания нового человека и сохранения созданных данных **/
    @PostMapping("people")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()) // проверка валидности пришедших с формы данных и если есть ошибки то возврат к созданию нового человека
            return "people/new"; //  возврат к созданию нового человека
        personDAO.save(person);  // сохранение данных
            return "redirect:/";
    }


    /** редактирование человека по id!  GET запрос: /id/edit **/
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id) // извлекаем id из url адреса @PathVariable и затем этот id  помещаем в аргумент int id в метод edit()
    {
        model.addAttribute("person", personDAO.show(id)); // аттрибут имеет ключ: "person", а в качестве значения будет то что вернёться изpersonDAO.show(id) по id
        return "people/edit";
    }

    /** метод обновления данных о человеке **/
    @PostMapping("people/{id}") // метод доступен по URL адресу: /id
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") Integer id)
    {
        if(bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person); // обновление данных о человеке
        return "redirect:/";
    }

    /** удаление человека **/
    @PostMapping("/{id}") // метод доступен по URL адресу: /id
    public String delete(@PathVariable("id") Integer id)
    {
        personDAO.delete(id);
        return "redirect:/";
    }
}


package com.example.daocrud.controllers;


import com.example.daocrud.dao.PersonDAO;
import com.example.daocrud.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class PeopleController
{

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }

    @GetMapping("/people")
    public String index(Model model)
    {
        // получим всех дюдей из DAO и передадим их на отображение в представление
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("people/{id}")
    public String Show(@PathVariable("id") int id, Model model)
    {
        // получим одного человека по id из DAO и передадим его на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("people/new")
    public String newPerson(@ModelAttribute("person") Person person)
    {
        // создание нового человека
        return "people/new";
    }
    @PostMapping("people")
    public String create(@ModelAttribute("person") Person person)
    {
        // сохранение данных
        personDAO.save(person);
        return "redirect:people";
    }

    @GetMapping("people/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id)
    {
        // редактировани человека по id
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    //@PatchMapping
    @PostMapping("people/{id}")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id)
    {
        // обновление данных о человеке
        personDAO.update(id, person);
        return "redirect:/people";
    }
    //@DeleteMapping
    @DeleteMapping("people/{id}")
    public String delete(@PathVariable("id") int id)
    {
        //удаление человека
        personDAO.delete(id);
        return "redirect/people";
    }
}

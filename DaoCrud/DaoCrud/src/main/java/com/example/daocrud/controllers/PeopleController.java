package com.example.daocrud.controllers;


import com.example.daocrud.dao.PersonDAO;
import com.example.daocrud.models.Person;
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

    @GetMapping("/people")
    public String index(Model model)
    {
        // получим всех дюдей из DAO и передадим их на отображение в представление
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    /** показать всех людей **/
    @GetMapping("people/{id}")
    public String Show(@PathVariable("id") int id, Model model)
    {
        // получим одного человека по id из DAO и передадим его на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    /** создать нового человека **/
    @GetMapping("people/new")
    public String newPerson(@ModelAttribute("person") Person person)
    {
        // создание нового человека
        return "people/new";
    }

    /** метод создания нового человека и сохранения созданных данных **/
    @PostMapping("people")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()) // проверка валидности
            // пришедших с формы данных
            // и если есть ошибки то возврат к созданию нового человека
            return "people/new"; //  возврат к созданию нового человека

        // сохранение данных
        personDAO.save(person);
        return "redirect:people";
    }


    /** редактирование человека по GET запросу: people/id/edit **/
    @GetMapping("people/{id}/edit") // метод помечен как GET запрос, доступный по адресу URL: people/id/edit
    public String edit(Model model, @PathVariable("id") int id) // извлекаем id из url адреса @PathVariable
    // и затем этот id  помещаем в аргумент int id в метод edit()
    {
        /** редактировани человека по id **/
        model.addAttribute("person", personDAO.show(id)); // аттрибут имеет ключ: "person",
        // а в качестве значения будет то что вернёться изpersonDAO.show(id) по id

        return "people/edit"; // место нахождение представления, т.е. файла.html,
        // который отвечает за показ страницы в браузере
        // Представление называеться edit.html и лежит в папке people
        // (URL адрес представления: people/id/edit)
    }


    /** метод обновления данных о человеке **/
    //@PatchMapping("people/{id}")
    @PostMapping("people/{id}") // метод доступен по URL адресу: people/id
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id)
    {
        if(bindingResult.hasErrors())
            return "people/edit";
        // обновление данных о человеке
        personDAO.update(id, person);
        return "redirect:/people";
    }


    /** удаление человека **/
    //@DeleteMapping
    @DeleteMapping("people/{id}") // метод доступен по URL адресу: people/id
    public String delete(@PathVariable("id") int id)
    {
        //удаление человека
        personDAO.delete(id);
        return "redirect/people";
    }
}

package com.example.daocrud.controllers;

import com.example.daocrud.JdbcPersonRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import com.example.daocrud.dao.PersonDAO;
import com.example.daocrud.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;


@Controller
public class PeopleController
{
    //region Field
    private JdbcPersonRepository jdbcPersonRepository;
    private JdbcTemplate jdbcTemplate;
    private final PersonDAO personDAO;

    private static final Logger LOGGER = Logger.getLogger(PeopleController.class.getName());
    //endregion

    //region Constructor
    @Autowired
    public PeopleController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate) {
        this.personDAO = personDAO;
        this.jdbcPersonRepository = jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;

    }
    //endregion


    /** показ всех людей из Базы Данных! (Выполнение запроса: "SELECT * FROM person") **/
    @GetMapping("/")
    public String index(Model model) // получим всех дюдей из DAO и передадим их на отображение в представление
    {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    /** показать человека из БД по заданному id! (Выполнение запроса: "SELECT * FROM person WHERE id=?") **/
    @GetMapping("people/{id}")
    public String Show(@PathVariable("id") Integer id, Model model) // получим одного человека по id из DAO и передадим его на отображение в представление
    {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }


    /** создать нового человека из БД (представление: "new.html", выводит поля для ввода данных) **/
    @GetMapping("people/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }


    /** метод создания нового человека и сохранения созданных данных!!!!!!!!!!!!!!!!!!!! **/
    @PostMapping("/people")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) // проверка валидности пришедших с формы данных и если есть ошибки то возврат к созданию нового человека
            return "people/new"; //  возврат к созданию нового человека
        personDAO.save(person);  // сохранение данных
        return "redirect:/";
    }


    /** редактирование человека по id!  GET запрос: /people/id/edit **/
    @GetMapping("/people/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id) // извлекаем id из url адреса @PathVariable и затем этот id  помещаем в аргумент int id в метод edit()
    {
        model.addAttribute("person", personDAO.show(id)); // аттрибут имеет ключ: "person", а в качестве значения будет то что вернёться из personDAO.show(id) по id
        return "people/edit";
    }

    /** метод обновления данных о человеке **/
    @PostMapping("/people/{id}") // метод доступен по URL адресу: /people/id
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person); // обновление данных о человеке
        return "redirect:/";
    }


    /** удаление человека **/
    @PostMapping("people/{id}/delete") // метод доступен по URL адресу: /people/id/delete
    public String delete(@PathVariable("id") Integer id) {
        personDAO.delete(id);
        return "redirect:/";
    }


    /** Отказ от создания нового человека в БД и переход к списку из БД **/
    @GetMapping("/people/refuse")
    public String refuse()
    {
        return "redirect:/";
    }

}



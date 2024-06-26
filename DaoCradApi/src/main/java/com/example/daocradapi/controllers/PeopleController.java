package com.example.daocradapi.controllers;

import com.example.daocradapi.repositories.JdbcPersonRepository;
import com.example.daocradapi.dao.messageEntity.MessageEntityDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import com.example.daocradapi.dao.person.PersonDAO;
import com.example.daocradapi.models.person.Person;
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
    private final MessageEntityDAO messageEntityDAO;
    private static final Logger LOGGER = Logger.getLogger(PeopleController.class.getName());
    //endregion

    //region Constructor
    @Autowired
    public PeopleController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate, MessageEntityDAO messageEntityDAO) {
        this.personDAO = personDAO;
        this.jdbcPersonRepository = jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.messageEntityDAO = messageEntityDAO;
    }
    //endregion

    /** показ всех людей из Базы Данных! (Выполнение запроса: "SELECT * FROM person2") **/
    @GetMapping("/people")
    public String index(Model model) // получим всех людей из DAO и передадим их на отображение в представление
    {
        model.addAttribute("people", personDAO.index());
        return "people/index"; // показ всех зарегистрированных пользователей из базы данных
    }

    /** показать человека из БД по заданному id! (Выполнение запроса: "SELECT * FROM person2 WHERE id=?") **/
    @GetMapping("/people/{id}")
    public String Show(@PathVariable("id") Integer id, Model model) // получим одного человека по id из DAO и передадим его на отображение в представление
    {
        model.addAttribute("person", personDAO.show(id));
        return "people/show"; // показ конкретного человека из базы данных по его id
    }

    /** создать нового человека из БД (представление: "new.html", выводит поля для ввода данных) **/
    @GetMapping("/people/new")
    public String newPerson(@ModelAttribute("person") Person person)
    {
        return "people/new"; // создание нового человека в базе данных
    }

    /** метод создания нового человека и сохранения созданных данных!!!!!!!!!!!!!!!!!!!! **/
    @PostMapping("/people")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) // проверка валидности пришедших с формы данных и если есть ошибки то возврат к созданию нового человека
            return "people/new"; //  возврат к созданию нового человека
        personDAO.save(person);  // сохранение данных
        return "redirect:/people"; // возврат к списку всех зарегистрированных пользователей
    }

    /** редактирование человека по id!  GET запрос: /id/edit **/
    @GetMapping("/people/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id) // извлекаем id из url адреса @PathVariable и затем этот id  помещаем в аргумент int id в метод edit()
    {
        model.addAttribute("person", personDAO.show(id)); // аттрибут имеет ключ: "person", а в качестве значения будет то что вернётся из personDAO.show(id) по id
        return "people/edit"; // редактирование пользователя
    }

    /** метод обновления данных о человеке **/
    @PostMapping("/people/{id}") // метод доступен по URL адресу: /people/id
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors())
            return "people/edit"; // обновление пользователя
        personDAO.update(id, person); // обновление данных о человеке
        return "redirect:/people";
    }

    /** удаление человека **/
    @PostMapping("/people/{id}/delete") // метод доступен по URL адресу: people/id/delete
    public String delete(@PathVariable("id") Integer id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

    /** Отказ от создания нового человека в БД и переход к списку из БД **/
    @PostMapping("people/refuse")
    public String refuse()
    {
        return "redirect:/people"; // переход на вывод всех пользователей из базы данных
    }
}


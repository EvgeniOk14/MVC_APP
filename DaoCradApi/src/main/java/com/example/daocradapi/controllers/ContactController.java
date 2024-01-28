package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.MessageEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.logging.Logger;

/** ------------------------- блок бработки данных из формы на странице Контакты --------------------------------------------- **/

@Controller
public class ContactController
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
    public ContactController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
    {
        this.personDAO = personDAO;
        this.jdbcPersonRepository= jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.messageEntityDAO = messageEntityDAO;
    }
    //endregion


    /** метод для обработки данных из формы: **/
    @PostMapping("/contacts/getform")
    public String processContactForm(@ModelAttribute("messageEntity") @Valid MessageEntity messageEntity, BindingResult bindingResult, Model model)
    {
        /** проверка на ошибку ввода, т.е. валидации **/
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Произошла ошибка валидации!");
            return "shop/contacts";
        }
        /** поиск пользователя в БД person2 с email введённым в форму на странице Контакты **/
        try
        {
            // поиск поля person_id  т.е. анешнего ключа, свзязанного полем id  таблице person2
            Integer person_Id = jdbcTemplate.queryForObject("SELECT id FROM person2 WHERE email = ?", new Object[]{messageEntity.getEmail()}, Integer.class);
            System.out.println("Found personId: " + person_Id); // Вывести personId в консоль для отладки

            // персона найдена и происходит процесс сохранения сообщения с формы в таблицу message
            messageEntityDAO.saveContactForm1(messageEntity, person_Id);
            model.addAttribute("message", "Форма успешно отправлена!");
            return "shop/contacts"; // возврат на страницу Контакты (представление: contacts.html)
        }
        catch (EmptyResultDataAccessException e)
        {
            // Пользователеь с таким email не найден, соответственно возврат к страницу Регистрация (представление: registration.html)
            return "redirect:/shop/registration"; // возврат на страницу Регистрация (представление: registration.html)
        }
    }
}

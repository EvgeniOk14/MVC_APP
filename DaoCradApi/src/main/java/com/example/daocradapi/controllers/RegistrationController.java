package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.logging.Logger;

@Controller
public class RegistrationController
{
    private JdbcPersonRepository jdbcPersonRepository;
    private JdbcTemplate jdbcTemplate;
    private final PersonDAO personDAO;
    private final MessageEntityDAO messageEntityDAO;
    private static final Logger LOGGER = Logger.getLogger(PeopleController.class.getName());
    //endregion

    //region Constructor
    public RegistrationController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
    {
        this.personDAO = personDAO;
        this.jdbcPersonRepository= jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.messageEntityDAO = messageEntityDAO;
    }


    /** регистрация пользователей сайта магазина (метод 1):
     *  Отображает страницу регистрации магазина.
     *  Когда пользователь делает GET-запрос к "/shop/registration",
     *  этот метод выполняется, создает новый объект Person,
     *  добавляет его в модель и возвращает имя представления,
     *  которое будет отображено пользователю (в данном случае, "shop/registration") **/
    @GetMapping("shop/registration")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("person", new Person());
        return "shop/registration"; //переход на форму с регистрацией пользователя
    }

    /** регистрация пользователей сайта магазина (метод 2):
     * считывание данных с формы (registration.html путь в форме: "@{/registration/error}" )
     * и сохранение пользователя магазина в базе данных,
     *  а также валидация введённых данных  **/
    @PostMapping("/registration/error")
    public String processRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                                      Model model)
    {
        if(bindingResult.hasErrors())
        {
            LOGGER.warning("Validation errors occurred: " + bindingResult.getAllErrors());
            model.addAttribute("message", "     Произошла ошибка валидации!");
            return "shop/registration"; // переход на форму с регистрацией пользователя
        }
        // Проверка наличия почты в БД
        if (jdbcPersonRepository.findPersonByEmail(person.getEmail())!=null)
        {
            model.addAttribute("message", "Такой адрес электронной почты уже используется.");
            return "shop/registration"; // возвращаем страницу с формой, чтобы пользователь мог внести коррективы.
        }
        personDAO.save(person);
        return "redirect:/"; // возвращает на главную страницу магазина
    }
}

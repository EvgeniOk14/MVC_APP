package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.models.MessageEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class PeopleController
{
    //region Field
    private  JdbcPersonRepository jdbcPersonRepository;
    private JdbcTemplate jdbcTemplate;
    private final PersonDAO personDAO;
    private final MessageEntityDAO messageEntityDAO;
    private static final Logger LOGGER = Logger.getLogger(PeopleController.class.getName());
    //endregion

    //region Constructor
    @Autowired
    public PeopleController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
    {
        this.personDAO = personDAO;
        this.jdbcPersonRepository= jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.messageEntityDAO = messageEntityDAO;
    }
    //endregion

    /**------------------------------блок отображения страниц магазина--------------------------------------------**/

    /** отображаем главную страницу магазина **/
    @GetMapping("/")
    public String showStartPage()
    {
        return "shop/shop";
    }

    /** отображаем страницу магазина Каталаог **/
    @GetMapping("/catalog")
    public String showCatalogPage()
    {
        return "shop/catalog";
    }



    /** --------------------------------блок Registraition --------------------------------------- **/

    /** регистрация пользователей сайта магазина (метод 1):
     *  Отображает страницу регистрации магазина.
     *  Когда пользователь делает GET-запрос к "/shop/registration",
     *  этот метод выполняется, создает новый объект Person,
     *  добавляет его в модель и возвращает имя представления,
     *  которое будет отображено пользователю (в данном случае, "shop/registration") **/
    @GetMapping("shop/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("person", new Person());
        return "shop/registration";
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
            return "shop/registration"; // изменения добавил shop в путь для того чтобы валидация работала!
        }
        // Проверка наличия почты в БД
        if (jdbcPersonRepository.findPersonByEmail(person.getEmail())!=null)
        {
            model.addAttribute("message", "Такой адрес электронной почты уже используется.");
            return "shop/registration"; // возвращаем страницу с формой, чтобы пользователь мог внести коррективы.
        }
        personDAO.save(person);
        return "redirect:/";
    }



    /** -----------------------------------блок People ----------------------------------------------- **/

    /** показ всех людей из Базы Данных! (Выполнение запроса: "SELECT * FROM person2") **/
    @GetMapping("/people")
    public String index(Model model) // получим всех дюдей из DAO и передадим их на отображение в представление
    {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    /** показать человека из БД по заданному id! (Выполнение запроса: "SELECT * FROM person2 WHERE id=?") **/
    @GetMapping("/people/{id}")
    public String Show(@PathVariable("id") Integer id, Model model) // получим одного человека по id из DAO и передадим его на отображение в представление
    {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }


    /** создать нового человека из БД (представление: "new.html", выводит поля для ввода данных) **/
    @GetMapping("/people/new")
    public String newPerson(@ModelAttribute("person") Person person)
        {
            return "people/new";
        }

    /** метод создания нового человека и сохранения созданных данных!!!!!!!!!!!!!!!!!!!! **/
    @PostMapping("/people")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()) // проверка валидности пришедших с формы данных и если есть ошибки то возврат к созданию нового человека
            return "people/new"; //  возврат к созданию нового человека
        personDAO.save(person);  // сохранение данных
            return "redirect:/people";
    }

    /** редактирование человека по id!  GET запрос: /id/edit **/
    @GetMapping("/people/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id) // извлекаем id из url адреса @PathVariable и затем этот id  помещаем в аргумент int id в метод edit()
    {
        model.addAttribute("person", personDAO.show(id)); // аттрибут имеет ключ: "person", а в качестве значения будет то что вернёться изpersonDAO.show(id) по id
        return "people/edit";
    }

    /** метод обновления данных о человеке **/
    @PostMapping("/people/{id}") // метод доступен по URL адресу: /id
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") Integer id)
    {
        if(bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person); // обновление данных о человеке
        return "redirect:/people";
    }

    /** удаление человека **/
    @PostMapping("/people/{id}/delete") // метод доступен по URL адресу: /id
    public String delete(@PathVariable("id") Integer id)
    {
        personDAO.delete(id);
        return "redirect:/people";
    }
    /** Отказ от создания нового человека в БД и переход к списку из БД **/
    @PostMapping("people/refuse")
    public String refuse()
    {
        return "redirect:/people";
    }




/** ----------------------------------- блок Контакты ----------------------------------------------------- **/

    /** Отображаем страницу магазина Контакты **/
    @GetMapping("/contacts")
    public String showContactsPage(Model model)
    {
        MessageEntity messageEntity = new MessageEntity();
        model.addAttribute("messageEntity", messageEntity);
        return "shop/contacts";
    }

    /** метод для обработки данных из формы: **/
    @PostMapping("/contacts/getform")
    public String processContactForm(@ModelAttribute("messageEntity") @Valid MessageEntity messageEntity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Произошла ошибка валидации!");
            return "shop/contacts";
        }

        try {
            Integer person_Id = jdbcTemplate.queryForObject(
                    "SELECT id FROM person2 WHERE email = ?",
                    new Object[]{messageEntity.getEmail()},
                    Integer.class);
            System.out.println("Found personId: " + person_Id); // Вывести personId в консоль для отладки

            // Person found, proceed with saving the message
            messageEntityDAO.saveContactForm(messageEntity, person_Id);
            model.addAttribute("message", "Форма успешно отправлена!");
            return "shop/contacts";
        } catch (EmptyResultDataAccessException e) {
            // Person not found, redirect to registration
            return "redirect:/shop/registration";
        }
    }


    /** метод показа сообщения message **/
    @GetMapping("/people/{personId}/messages")
    public String showMessages(@PathVariable Integer personId, Model model) {
        List<MessageEntity> messages = messageEntityDAO.getMessagesByPersonId(personId);
        model.addAttribute("messages", messages);
        return "people/messages";
    }



/** -----------------------------------------блок Errors-------------------------------------------------------- **/

    /** выведение сообщений об ошибках совершённых пользователем магазина для отладки **/
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model)
    {
        e.printStackTrace();
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}



     /** ------------------------- пример использования вывода любого сообщения представление ------**/
//    @GetMapping("/shop/start")
//    public String showStartPage(Model model)
//    {
//        model.addAttribute("message", "Привет, мир!");
//        return "people/shop/start";
//    }
   /**----------------------------------------------------------------------------------------------**/
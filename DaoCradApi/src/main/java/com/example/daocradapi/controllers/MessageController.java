package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.MessageEntity;
import com.example.daocradapi.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.logging.Logger;

/**-----------------------------------------------блок Messages-----------------------------------------------------------------**/

@Controller
@RequestMapping("/people")
public class MessageController
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
    public MessageController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
    {
        this.personDAO = personDAO;
        this.jdbcPersonRepository= jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.messageEntityDAO = messageEntityDAO;
    }
    //endregion

    /** метод показа сообщения message **/
    //@GetMapping("/people/{personId}/messages")
    @GetMapping("/{personId}/messages")
    public String showMessages(@PathVariable Integer personId, Model model) {
        List<MessageEntity> messages = messageEntityDAO.getMessagesByPersonId(personId);
        model.addAttribute("messages", messages);
        return "people/messages";
    }

    /** показ страницы с сообщениями **/
    //@GetMapping("/people/messages")
    @GetMapping("/messages") // показ сообщений из таблицы essages с данными из таблицы person2
    public String showMessagesPage(Model model)
    {
        List<MessageEntity> messages = messageEntityDAO.getAllMessages();

        // Для каждого сообщения получаем данные из таблицы person2
        for (MessageEntity message : messages)
        {
            Person person = personDAO.getPersonById(message.getPerson_id());
            message.setPerson(person);
        }

        model.addAttribute("MessageEntity", messages);
        return "people/messages";
    }

}



//    /** показ страницы с сообщениями **/
//    @GetMapping("/people/messages") // без данных из таблицы person2
//    public String showMessagesPage(Model model)
//    {
//        model.addAttribute("MessageEntity", messageEntityDAO.getAllMessages());
//        return "people/messages";
//    }
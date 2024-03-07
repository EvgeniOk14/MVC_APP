package com.example.daocradapi.controllers;

import com.example.daocradapi.repositories.JdbcPersonRepository;
import com.example.daocradapi.dao.messageEntity.MessageEntityDAO;
import com.example.daocradapi.dao.person.PersonDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.logging.Logger;

@Controller
public class GlobalExceptionHandlerController
{
    //region Field
    private JdbcPersonRepository jdbcPersonRepository;
    private JdbcTemplate jdbcTemplate;
    private final PersonDAO personDAO;
    private final MessageEntityDAO messageEntityDAO;
    private static final Logger LOGGER = Logger.getLogger(PeopleController.class.getName());
    //endregion

    //region Constructor
    public GlobalExceptionHandlerController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
    {
        this.personDAO = personDAO;
        this.jdbcPersonRepository= jdbcPersonRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.messageEntityDAO = messageEntityDAO;
    }
    //endregion

    @ControllerAdvice
    public class  GlobalExceptionHandler
    {
        private static final Logger LOGGER = Logger.getLogger(com.example.daocradapi.controllers.GlobalExceptionHandlerController.class.getName());

        @ExceptionHandler(Exception.class)
        public String handleException(Exception e, Model model)
        {
            e.printStackTrace();
            LOGGER.warning("An exception occurred: " + e.getMessage());
            model.addAttribute("error", "An error occurred. Please try again later.");
            return "error";
        }
    }
}

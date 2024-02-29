package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.dao.PersonDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import java.util.logging.Logger;

/**--------------------------------------базовый абстрактный контроллер---------------------------------------------------------**/
@Controller
public abstract class BaseController
{
        //region Field
        private JdbcPersonRepository jdbcPersonRepository;
        private JdbcTemplate jdbcTemplate;
        private final PersonDAO personDAO;
        private final MessageEntityDAO messageEntityDAO;
        private static final Logger LOGGER = Logger.getLogger(com.example.daocradapi.controllers.PeopleController.class.getName());
        //endregion

        //region Constructor
        public BaseController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
        {
            this.personDAO = personDAO;
            this.jdbcPersonRepository= jdbcPersonRepository;
            this.jdbcTemplate = jdbcTemplate;
            this.messageEntityDAO = messageEntityDAO;
        }
        //endregion
}

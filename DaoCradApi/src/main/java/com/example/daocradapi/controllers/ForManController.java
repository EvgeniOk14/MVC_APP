package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class ForManController
{
        //region Field
        private JdbcPersonRepository jdbcPersonRepository;
        private JdbcTemplate jdbcTemplate;
        private final PersonDAO personDAO;
        private final MessageEntityDAO messageEntityDAO;
        private static final Logger LOGGER = Logger.getLogger(com.example.daocradapi.controllers.PeopleController.class.getName());
        //endregion

        //region Constructor
        @Autowired
        public ForManController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
        {
            this.personDAO = personDAO;
            this.jdbcPersonRepository= jdbcPersonRepository;
            this.jdbcTemplate = jdbcTemplate;
            this.messageEntityDAO = messageEntityDAO;
        }
        //endregion

    /**отображает мужские спортивные костюмы**/
    @GetMapping("/AddedHtmlSites/indexSportSuitMan")
    public String clotheForMan()
    {
        return "shop/AddedHtmlSites/indexSportSuitMan";
    }

    /**отображает мужские кросовки**/
    @GetMapping("/AddedHtmlSites/indexCrossMan")
    public String crossForMan()
    {
        return "shop/AddedHtmlSites/indexCrossMan";
    }


    /**----------------------------блок спортивные костюмы мужские------------------------------------------------ **/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManSuit1")
    public String SuitForMan1()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit1";
    }

    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManSuit2")
    public String SuitForMan2()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit2";
    }

    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManSuit3")
    public String SuitForMan3()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit3";
    }

    /**----------------------------------- кросовки мужские------------------------------------------------------**/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManCross1")
    public String CrossForMan1()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross1";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManCross2")
    public String CrossForMan2()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross2";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManCross3")
    public String CrossForMan3()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross3";
    }

}
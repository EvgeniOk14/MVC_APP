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
public class ForWomanController
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
        public ForWomanController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository, JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO)
        {
            this.personDAO = personDAO;
            this.jdbcPersonRepository= jdbcPersonRepository;
            this.jdbcTemplate = jdbcTemplate;
            this.messageEntityDAO = messageEntityDAO;
        }
        //endregion
    
    /**отображает женские спортивные костюмы**/
    @GetMapping("/AddedHtmlSites/indexSportSuitWoman")
    public String clotheForWoman()
    {
        return "shop/AddedHtmlSites/indexSportSuitWoman";
    }

    /**отображает женские кросовки**/
    @GetMapping("/AddedHtmlSites/indexCrossWoman")
    public String crossForWoman()
    {
        return "shop/AddedHtmlSites/indexCrossWoman";
    }

    /**----------------------------блок спортивные костюмы мужские------------------------------------------------ **/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanSuit1")
    public String SuitForWoman1()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit1";
    }

    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanSuit2")
    public String SuitForWoman2()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit2";
    }

    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanSuit3")
    public String SuitForWoman3()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit3";
    }



    /**----------------------------------- отображает женские кросовки ------------------------------------------------------**/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanCross1")
    public String CrossForWoman1()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross1";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanCross2")
    public String CrossForWoman2()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross2";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanCross3")
    public String CrossForWoman3()
    {
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross3";
    }


}






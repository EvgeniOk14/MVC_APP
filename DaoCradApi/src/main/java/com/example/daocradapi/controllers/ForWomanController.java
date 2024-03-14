package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.products.NewThing;
import com.example.daocradapi.repositories.JdbcPersonRepository;
import com.example.daocradapi.dao.messageEntity.MessageEntityDAO;
import com.example.daocradapi.dao.person.PersonDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class ForWomanController
{
        //region Field
        private JdbcPersonRepository jdbcPersonRepository;
        private JdbcTemplate jdbcTemplate;
        private final PersonDAO personDAO;
        private ThingDAO thingDAO;
        private final MessageEntityDAO messageEntityDAO;
        private static final Logger LOGGER = Logger.getLogger(com.example.daocradapi.controllers.PeopleController.class.getName());
        //endregion

        //region Constructor
        public ForWomanController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository,
                                  JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO,
                                  ThingDAO thingDAO)
        {
            this.personDAO = personDAO;
            this.jdbcPersonRepository= jdbcPersonRepository;
            this.jdbcTemplate = jdbcTemplate;
            this.messageEntityDAO = messageEntityDAO;
            this.thingDAO = thingDAO;
        }
        //endregion
    
    /**--------------------отображает страницу со всеми женскими спортивными костюмами-------------------------------**/
    @GetMapping("/AddedHtmlSites/indexSportSuitWoman")
    public String clotheForWoman()
    {
        return "shop/AddedHtmlSites/indexSportSuitWoman";
    }

    /**---------------------отображает страницу со всеми женскими кросовками-----------------------------------------**/
    @GetMapping("/AddedHtmlSites/indexCrossWoman")
    public String crossForWoman()
    {
        return "shop/AddedHtmlSites/indexCrossWoman";
    }



    /**---------------------блок спортивные костюмы женские по одному товару на странице-----------------------------**/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanSuit1")
    public String SuitForWoman1(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanSuit", "бежевый", Gender.FEMALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit1";
    }


    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanSuit2")
    public String SuitForWoman2(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanSuit", "красный", Gender.FEMALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit2";
    }

    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanSuit3")
    public String SuitForWoman3(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanSuit", "чёрный", Gender.FEMALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit3";
    }

    /**---------------------блок женские кросовки по одному товару на странице---------------------------------------**/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanCross1")
    public String CrossForWoman1(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanCross", "розовый", Gender.FEMALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross1";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanCross2")
    public String CrossForWoman2(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanCross", "белый", Gender.FEMALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross2";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/WomanCross3")
    public String CrossForWoman3(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanCross", "серый", Gender.FEMALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross3";
    }
}







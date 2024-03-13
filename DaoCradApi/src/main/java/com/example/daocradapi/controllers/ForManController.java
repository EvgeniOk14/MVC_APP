package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.cart.CartDAO;
import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.person.Person;
import com.example.daocradapi.models.products.NewThing;
import com.example.daocradapi.repositories.JdbcPersonRepository;
import com.example.daocradapi.dao.messageEntity.MessageEntityDAO;
import com.example.daocradapi.dao.person.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class ForManController
{
        //region Field
        private JdbcPersonRepository jdbcPersonRepository;
        private JdbcTemplate jdbcTemplate;
        private final PersonDAO personDAO;
        private final MessageEntityDAO messageEntityDAO;
        private ThingDAO thingDAO;
        private CartDAO cartDAO;
        private static final Logger LOGGER = Logger.getLogger(com.example.daocradapi.controllers.PeopleController.class.getName());
       private Gender MALE;


        //endregion

        //region Constructor
        @Autowired
        public ForManController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository,
                                JdbcTemplate jdbcTemplate,  MessageEntityDAO messageEntityDAO,
                                ThingDAO thingDAO, CartDAO cartDAO)
        {
            this.personDAO = personDAO;
            this.jdbcPersonRepository= jdbcPersonRepository;
            this.jdbcTemplate = jdbcTemplate;
            this.messageEntityDAO = messageEntityDAO;
            this.thingDAO = thingDAO;
            this.cartDAO = cartDAO;
        }
        //endregion

    /**--------------------------отображает страницу со всеми мужскими спортивными костюмами-------------------------**/
    @GetMapping("/AddedHtmlSites/indexSportSuitMan")
    public String clotheForMan()
    {
        return "shop/AddedHtmlSites/indexSportSuitMan";
    }

    /**-------------------------отображает страницу со всеми мужскими кросовками-------------------------------------**/
    @GetMapping("/AddedHtmlSites/indexCrossMan")
    public String crossForMan()
    {
        return "shop/AddedHtmlSites/indexCrossMan";
    }



    /**----------------------------блок спортивные костюмы мужские по одному товару на странице--------------------- **/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManSuit1")
    public String SuitForMan1(Model model)
    {
        List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender(); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit1";
    }



    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManSuit2")
    public String SuitForMan2(Model model)
    {
        List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManSuit", "синий", Gender.MALE); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        System.out.println("availableSizes" + availableSizes);
        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit2";
    }


    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManSuit3")
    public String SuitForMan3(Model model)
    {
        List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender(); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit3";
    }

    /**----------------------------блок кросовки мужские по одному товару на странице--------------------------------**/
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManCross1")
    public String CrossForMan1(Model model)
    {
        List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender(); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross1";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManCross2")
    public String CrossForMan2(Model model)
    {
        List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender(); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross2";
    }
    @GetMapping("/AddedHtmlSites/CatalogOfArticle/ManCross3")
    public String CrossForMan3(Model model)
    {
        List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender(); // находим список вещей с одинаковым именем, цветом и полом
        model.addAttribute("availableSizes", availableSizes); // добавляем найденный список вещей в модель
        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross3";
    }

}

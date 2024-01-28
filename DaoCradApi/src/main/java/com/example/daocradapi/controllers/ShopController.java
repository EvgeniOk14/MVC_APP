package com.example.daocradapi.controllers;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.dao.MessageEntityDAO;
import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**------------------------------блок отображения страниц магазина--------------------------------------------**/

@Controller
@RequestMapping("/")
public class ShopController extends BaseController
{
    @Autowired
    public ShopController(PersonDAO personDAO, JdbcPersonRepository jdbcPersonRepository,
                          JdbcTemplate jdbcTemplate, MessageEntityDAO messageEntityDAO)
    {
        super(personDAO, jdbcPersonRepository, jdbcTemplate, messageEntityDAO);
    }


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

    /** Отображаем страницу магазина Контакты **/
    @GetMapping("/contacts")
    public String showContactsPage(Model model)
    {
        MessageEntity messageEntity = new MessageEntity();
        model.addAttribute("messageEntity", messageEntity);
        return "shop/contacts";
    }
}

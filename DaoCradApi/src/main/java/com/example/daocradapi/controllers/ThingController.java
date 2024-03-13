package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.cart.CartDAO;
import com.example.daocradapi.dao.person.PersonDAO;
import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.person.Person;
import com.example.daocradapi.models.products.*;
import com.example.daocradapi.repositories.JdbcPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ThingController
{
    //region Fields
    @Autowired
    private ThingDAO thingDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private JdbcPersonRepository jdbcPersonRepository;
    //endregion

    /**
     * Показ всего списка вещей из каталога (БД)
     **/
    @GetMapping("/things")
    public String getAllThigs(Model model) {
        List<Thing> things = thingDAO.getAllThigs();
        model.addAttribute("things", things);
        return "things/things";
    }

    /**
     * переход на форму для добавление новой вещи в каталог (БД)
     **/
    @GetMapping("/showFormThing")
    public String formThings(Model model, NewThing newThing) {
        model.addAttribute("newthing", newThing);
        return "things/showFormThing";
    }

    /**
     * добавление новой вещи в каталог (БД)
     **/
    @PostMapping("/saveThingsFromForm")
    public String saveThing(@ModelAttribute("newthing") NewThing newThing) {
        thingDAO.saveThing(newThing); // сохранения новой вещи
        return "redirect:/things"; // Перенаправление пользователя на список вещей после сохранения
    }

    /**
     * метод удаление вещи из каталога (БД)
     **/
    @GetMapping("/deleteThing/{thing_id}")
    public String deleteThing(@PathVariable("thing_id") Integer thing_id) {
        thingDAO.deleteThing(thing_id);
        return "redirect:/things";
    }

    /**
     * переход на форму редактирования вещи
     **/
    @GetMapping("/edit/{thing_id}")
    public String editForm(Model model, @PathVariable("thing_id") Integer thing_id) {
        model.addAttribute("newThing", thingDAO.getThingById(thing_id));
        return "things/editThing";
    }

    /** редактирование вещи по номеру thing_id **/
    @PostMapping("/editThing/{thing_id}/edit")
    public String editThing(@ModelAttribute("newThing") NewThing newThing, @PathVariable("thing_id") Integer thing_id) {
        thingDAO.editThing(thing_id, newThing);
        return "redirect:/things";
    }


    /**------------------------------------------блок выпадающий список:------------------------------------------------------**/


    /** метод добавления выбранной вещи из выпадающего списка размеров **/
    @PostMapping("/showThingWithSameNameColorGender/choiseSize")
    public String chooseThingWithSameNameColorGender(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSize";
    }

    /** метод позволяет добавить товар из выпадающего списка в корзину пользователя на любом представлении из одиночных вещей по типу (ManSuit1.html и прочие подобные представления) **/
    @PostMapping("/checkPerson1")
    public String showCheckPersonForm(@RequestParam("email") String email,
                                      @RequestParam("selectedThingId") Integer selectedThingId,
                                      Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id


            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя


            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
            currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину

            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

            List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender();

            boolean isAvailable = false;      // флаг на доступность товара
            boolean successAdding = false;   // флаг на успешное добавление товара
            boolean thingsIsAbsent = false; // флаг на то что товар закончился

            for (NewThing thing: availableSizes) // проходим по списку с доступными товарами
            {
                if(thing.getThing_id().equals(selectedThingId) && thing.getQuantity() > 0) // если у товара его количество на складе больше 0 (т.е. товар есть в наличии), то:
                {
                    thingDAO.removeQuantity(selectedThingId); // при добавлении товара в корзину, уменьшаем количество товара на единицу
                    isAvailable = true;
                    successAdding = true;
                    thingsIsAbsent = false;

                }
                if(thing.getThing_id().equals(selectedThingId) && thing.getQuantity() == 0) // если количество товара стало равно нулю (т.е. товар закончился), то:
                {
                    availableSizes.remove(thing); // удаляем выбранный товар из списка доступных товаров
                    isAvailable = false;
                    successAdding = true;
                    thingsIsAbsent = true;
                    break;                    // выход из цикла
                }
            }
            model.addAttribute("isAvailable", isAvailable);
            model.addAttribute("unavailableSize", selectedThing.getThing_size());
            model.addAttribute("availableSizes", availableSizes);
            model.addAttribute("successAdding", successAdding);
            model.addAttribute("thingsIsAbsent", thingsIsAbsent);
            model.addAttribute("selectedThing", selectedThing);
        }
        else
        {
            return "shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
        }

        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit1"; // Возвращаем представление с формой для ввода email
    }


    /** метод добавления выбранной вещи из выпадающего списка размеров **/
    @PostMapping("/showThingWithSameNameColorGender2/choiseSize")
    public String chooseThingWithSameNameColorGender2(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSize2";
    }
    @PostMapping("/checkPerson2")
    public String showCheckPersonForm2(@RequestParam("email") String email,
                                      @RequestParam("selectedThingId") Integer selectedThingId,
                                      Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id
            System.out.println("!!!!!!!!!!!!!!!!selectedThing: " + selectedThing);

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя


            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
            currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину

            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManSuit", "синий", Gender.MALE);
            System.out.println("!!!!!!!!!!!!!!!!!!!availableSizes :" + availableSizes);

            boolean isAvailable = false;      // флаг на доступность товара
            boolean successAdding = false;   // флаг на успешное добавление товара
            boolean thingsIsAbsent = false; // флаг на то что товар закончился

            for (NewThing thing: availableSizes) // проходим по списку с доступными товарами
            {
                if(thing.getThing_id().equals(selectedThingId) && thing.getQuantity() > 0) // если у товара его количество на складе больше 0 (т.е. товар есть в наличии), то:
                {
                    System.out.println("!!!!!!!!!!! ДО количество: " + thing.getQuantity());
                    thingDAO.removeQuantity(selectedThingId); // при добавлении товара в корзину, уменьшаем количество товара на единицу
                    System.out.println("!!!!!!!!!!! ПОСЛЕ количество: " + thing.getQuantity());
                    isAvailable = true;
                    successAdding = true;
                    thingsIsAbsent = false;

                }
                if(thing.getThing_id().equals(selectedThingId) && thing.getQuantity() == 0) // если количество товара стало равно нулю (т.е. товар закончился), то:
                {
                    System.out.println("!!!!!!!!!!!!!!!!!находимся в удалении вещи!!!!!!!!!!!!");
                    System.out.println("удаляемая вещь thing :" + thing);
                    availableSizes.remove(thing); // удаляем выбранный товар из списка доступных товаров
                    isAvailable = false;
                    successAdding = true;
                    thingsIsAbsent = true;
                    break;                    // выход из цикла
                }
            }
            model.addAttribute("isAvailable", isAvailable);
            model.addAttribute("unavailableSize", selectedThing.getThing_size());
            model.addAttribute("availableSizes", availableSizes);
            model.addAttribute("successAdding", successAdding);
            model.addAttribute("thingsIsAbsent", thingsIsAbsent);
            model.addAttribute("selectedThing", selectedThing);
        }
        else
        {
            return "shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
        }

        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit2"; // Возвращаем представление с формой для ввода email
    }




 /**--------------------------------------конец блока с выпадающим списком---------------------------------------------------**/
}













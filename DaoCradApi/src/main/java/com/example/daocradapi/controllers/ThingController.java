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
import java.util.ArrayList;
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

    /** Показ всего списка вещей из каталога (БД) **/
    @GetMapping("/things")
    public String getAllThigs(Model model)
    {
        List<NewThing> things = thingDAO.getAllThigs();
        model.addAttribute("things", things);
        return "things/things";
    }

    /** переход на форму для добавление новой вещи в каталог (БД) **/
    @GetMapping("/showFormThing")
    public String formThings(Model model, NewThing newThing)
    {
        model.addAttribute("newthing", newThing);
        return "things/showFormThing";
    }

    /** добавление новой вещи в каталог (БД) **/
    @PostMapping("/saveThingsFromForm")
    public String saveThing(@ModelAttribute("newthing") NewThing newThing)
    {
        thingDAO.saveThing(newThing); // сохранения новой вещи
        return "redirect:/things"; // Перенаправление пользователя на список вещей после сохранения
    }

    /** метод удаление вещи из каталога (БД) **/
    @GetMapping("/deleteThing/{thing_id}")
    public String deleteThing(@PathVariable("thing_id") Integer thing_id)
    {
        thingDAO.deleteThing(thing_id);
        return "redirect:/things";
    }

    /** переход на форму редактирования вещи **/
    @GetMapping("/edit/{thing_id}")
    public String editForm(Model model, @PathVariable("thing_id") Integer thing_id)
    {
        model.addAttribute("newThing", thingDAO.getThingById(thing_id));
        return "things/editThing";
    }

    /** редактирование вещи по номеру thing_id **/
    @PostMapping("/editThing/{thing_id}/edit")
    public String editThing(@ModelAttribute("newThing") NewThing newThing, @PathVariable("thing_id") Integer thing_id) {
        thingDAO.editThing(thing_id, newThing);
        return "redirect:/things";
    }



/**----------------------------------------блок выпадающий список:---------------------------------------------------**/

/**----------------------------------------блок представления ManSuit1-----------------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров для ManSuit1.html **/
    @PostMapping("/showThingWithSameNameColorGender/choiseSize")
    public String chooseThingWithSameNameColorGender(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSize";
    }

    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении ManSuit1.html  **/
    @PostMapping("/checkPerson1")
    public String showCheckPersonForm(@RequestParam("email") String email,
                                      @RequestParam("selectedThingId") Integer selectedThingId,
                                      Model model)
    {
        if(selectedThingId == null)
        {
            return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/ManSuit1"; // если вещь не выбрана, то возврат к выбору вещи
        }
        if (email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя
            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }

            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

            List<NewThing> availableSizes = thingDAO.getListThinsWithSameNameColorGender();

            boolean isAvailable = false;      // флаг на доступность товара
            boolean successAdding = false;   // флаг на успешное добавление товара
            boolean thingsIsAbsent = false; // флаг на то что товар закончился

            for (NewThing thing : availableSizes) // проходим по списку с доступными товарами
            {
                if (thing.getThing_id().equals(selectedThingId) && thing.getQuantity() > 0) // если у товара его количество на складе больше 0 (т.е. товар есть в наличии), то:
                {
                    thingDAO.removeQuantity(selectedThingId); // при добавлении товара в корзину, уменьшаем количество товара на единицу
                    isAvailable = true;
                    successAdding = true;
                    thingsIsAbsent = false;
                }
                if (thing.getThing_id().equals(selectedThingId) && thing.getQuantity() == 0) // если количество товара стало равно нулю (т.е. товар закончился), то:
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
/**----------------------------------конец блока представления ManSuit1----------------------------------------------**/



/**----------------------------------блок для представления ManSuit2-------------------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении ManSuit2.html **/
    @PostMapping("/showThingWithSameNameColorGender2/choiseSize")
    public String chooseThingWithSameNameColorGender2(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSize2";
    }

    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении ManSuit2.html  **/
    @PostMapping("/checkPerson2")
    public String showCheckPersonForm2(@RequestParam("email") String email,
                                      @RequestParam("selectedThingId") Integer selectedThingId,
                                      Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManSuit", "синий", Gender.MALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/ManSuit2"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit2"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления ManSuit2---------------------------------------**/



/**-------------------------------------начало блока для представления ManSuit3---------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении ManSuit3.html **/
    @PostMapping("/showThingWithSameNameColorGender3/choiseSize")
    public String chooseThingWithSameNameColorGender3(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSize3";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении ManSuit3.html  **/
    @PostMapping("/checkPerson3")
    public String showCheckPersonForm3(@RequestParam("email") String email,
                                       @RequestParam("selectedThingId") Integer selectedThingId,
                                       Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManSuit", "серый", Gender.MALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/ManSuit3"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/ManSuit3"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления ManSuit2---------------------------------------**/



/**-------------------------------------начало блока для представления ManCross1-------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении ManCross1.html **/
    @PostMapping("/showThingWithSameNameColorGenderManCross1/choiseSize")
    public String chooseThingWithSameNameColorGenderManCross1(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeManCross1";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении ManCross1.html  **/
    @PostMapping("/checkPersonManCross1")
    public String showCheckPersonFormManCross1(@RequestParam("email") String email,
                                       @RequestParam("selectedThingId") Integer selectedThingId,
                                       Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManCross", "синий", Gender.MALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/ManCross1"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross1"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления ManCross1--------------------------------------**/



/**-------------------------------------начало блока для представления ManCross2-------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении ManCross2.html **/
    @PostMapping("/showThingWithSameNameColorGenderManCross2/choiseSize")
    public String chooseThingWithSameNameColorGenderManCross2(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeManCross2";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении ManCross2.html  **/
    @PostMapping("/checkPersonManCross2")
    public String showCheckPersonFormManCross2(@RequestParam("email") String email,
                                               @RequestParam("selectedThingId") Integer selectedThingId,
                                               Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManCross", "голубой", Gender.MALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/ManCross2"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross2"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления ManCross2--------------------------------------**/



/**-------------------------------------начало блока для представления ManCross3-------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении ManCross3.html **/
    @PostMapping("/showThingWithSameNameColorGenderManCross3/choiseSize")
    public String chooseThingWithSameNameColorGenderManCross3(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeManCross3";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении ManCross3.html  **/
    @PostMapping("/checkPersonManCross3")
    public String showCheckPersonFormManCross3(@RequestParam("email") String email,
                                               @RequestParam("selectedThingId") Integer selectedThingId,
                                               Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("ManCross", "чёрный", Gender.MALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/ManCross3"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/ManCross3"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления ManCross3--------------------------------------**/



/**------------------------------------------------блок Woman--------------------------------------------------------**/

/**-------------------------------------начало блока для представления WomanSuit1------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении WomanSuit1.html **/
    @PostMapping("/showThingWithSameNameColorGenderWomanSuit1/choiseSize")
    public String chooseThingWithSameNameColorGenderWomanSuit1(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeWomanSuit1";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении WomanSuit1.html  **/
    @PostMapping("/checkPersonWomanSuit1")
    public String showCheckPersonFormWomanSuit1(@RequestParam("email") String email,
                                               @RequestParam("selectedThingId") Integer selectedThingId,
                                               Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanSuit", "бежевый", Gender.FEMALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/WomanSuit1"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit1"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления WomanSuit1-------------------------------------**/



/**-------------------------------------начало блока для представления WomanSuit2------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении WomanSuit2.html **/
    @PostMapping("/showThingWithSameNameColorGenderWomanSuit2/choiseSize")
    public String chooseThingWithSameNameColorGenderWomanSuit2(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeWomanSuit2";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении WomanSuit2.html  **/
    @PostMapping("/checkPersonWomanSuit2")
    public String showCheckPersonFormWomanSuit2(@RequestParam("email") String email,
                                                @RequestParam("selectedThingId") Integer selectedThingId,
                                                Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanSuit", "красный", Gender.FEMALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/WomanSuit2"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit2"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления WomanSuit2-------------------------------------**/



/**-------------------------------------начало блока для представления WomanSuit3------------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении WomanSuit3.html **/
    @PostMapping("/showThingWithSameNameColorGenderWomanSuit3/choiseSize")
    public String chooseThingWithSameNameColorGenderWomanSuit3(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeWomanSuit3";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении WomanSuit3.html  **/
    @PostMapping("/checkPersonWomanSuit3")
    public String showCheckPersonFormWomanSuit3(@RequestParam("email") String email,
                                                @RequestParam("selectedThingId") Integer selectedThingId,
                                                Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanSuit", "чёрный", Gender.FEMALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/WomanSuit3"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/WomanSuit3"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления WomanSuit3-------------------------------------**/



/**-------------------------------------начало блока для представления WomanCross1-----------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении WomanCross1.html **/
    @PostMapping("/showThingWithSameNameColorGenderWomanCross1/choiseSize")
    public String chooseThingWithSameNameColorGenderWomanCross1(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeWomanCross1";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представленииWomanCross1.html  **/
    @PostMapping("/checkPersonWomanCross1")
    public String showCheckPersonFormWomanCross1(@RequestParam("email") String email,
                                                @RequestParam("selectedThingId") Integer selectedThingId,
                                                Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanCross", "розовый", Gender.FEMALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/WomanCross1"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross1"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления WomanCross1------------------------------------**/



/**-------------------------------------начало блока для представления WomanCross2-----------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении WomanCross2.html **/
    @PostMapping("/showThingWithSameNameColorGenderWomanCross2/choiseSize")
    public String chooseThingWithSameNameColorGenderWomanCross2(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeWomanCross2";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении WomanCross2.html  **/
    @PostMapping("/checkPersonWomanCross2")
    public String showCheckPersonFormWomanCross2(@RequestParam("email") String email,
                                                 @RequestParam("selectedThingId") Integer selectedThingId,
                                                 Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanCross", "белый", Gender.FEMALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/WomanCross2"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross2"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления WomanCross2------------------------------------**/



/**-------------------------------------начало блока для представления WomanCross3-----------------------------------**/
    /** метод добавления выбранной вещи из выпадающего списка размеров на представлении WomanCross3.html **/
    @PostMapping("/showThingWithSameNameColorGenderWomanCross3/choiseSize")
    public String chooseThingWithSameNameColorGenderWomanCross3(@RequestParam("selectedThingId") Integer selectedThingId, Model model)
    {
        model.addAttribute("selectedThingId", selectedThingId); // добавляем в модель id выбранной вещи
        return "shop/checkRegistrationChoiseSizeWomanCross3";
    }
    /** метод позволяет добавить товар из выпадающего списка в корзину
     *  пользователя на представлении WomanCross3.html  **/
    @PostMapping("/checkPersonWomanCross3")
    public String showCheckPersonFormWomanCross3(@RequestParam("email") String email,
                                                 @RequestParam("selectedThingId") Integer selectedThingId,
                                                 Model model)
    {
        if(email != null) // если пользователь зарегистрирован, значит у него есть email, а значит ему можно добавить или получить корзину и прочее
        {
            List<NewThing> availableSizes = thingDAO.totalGetListThinsWithSameNameColorGender("WomanCross", "серый", Gender.FEMALE);

            if(selectedThingId == null)
            {
                return "redirect:/shop/AddedHtmlSites/CatalogOfArticle/WomanCross3"; // если вещь не выбрана, то возврат к выбору вещи
            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); //получаем выбранную вещь по её id

            Person currentPerson = personDAO.getPersonByEmail(email); // получаем текущего пользователя по его email
            if (currentPerson == null)
            {
                return "redirect:/shop/registration"; // направляем на регистрацию, в случае если пользователь не зарегистрирован
            }

            Integer person_id = currentPerson.getId(); // получаем id текущего пользователя

            Cart currentUserCart = cartDAO.getCartByUserId(person_id); // получаем корзину текущего пользователя

            if (currentUserCart == null)                    // Если корзина еще не существует
            {
                currentUserCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                currentUserCart.setListOfCartItems(new ArrayList<>()); // добавляем корзине список вещей
                currentUserCart.setPerson(currentPerson);  // Связываем корзину с текущим пользователем
                currentPerson.setCart(currentUserCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            else
            {
                currentUserCart.setPerson(currentPerson);  // устанавливаем корзине текущего пользователя
                currentPerson.setCart(currentUserCart);   // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(currentUserCart);     // Сохраняем созданную корзину в базе данных
            }
            cartDAO.addCartItemToCart(currentUserCart, selectedThing); // добавляем выбранную вещь в корзину к текущему пользователю

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

        return "shop/AddedHtmlSites/CatalogOfArticle/WomanCross3"; // Возвращаем представление с формой для ввода email
    }
/**-------------------------------------конец блока для представления WomanCross3------------------------------------**/

/**--------------------------------------конец блока с выпадающим списком--------------------------------------------**/
}













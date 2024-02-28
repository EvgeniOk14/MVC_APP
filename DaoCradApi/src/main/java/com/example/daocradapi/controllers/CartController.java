package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.dao.cart.CartDAO;
import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.Person;
import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController
{
    private List<NewThing> listOfnewThings = new ArrayList<>();
    private ThingDAO thingDAO;
    private CartDAO cartDAO;
    private PersonDAO personDAO;
    @PersistenceContext
    private final EntityManager entityManager;

    public CartController(List<NewThing> listOfnewThings, ThingDAO thingDAO, CartDAO cartDAO, PersonDAO personDAO, EntityManager entityManager)
    {
        this.listOfnewThings = listOfnewThings;
        this.thingDAO = thingDAO;
        this.cartDAO = cartDAO;
        this.personDAO = personDAO;
        this.entityManager = entityManager;
    }

    /** переход на представление checkRegistration.html для ввода email в форму,
     *  для последующей её отправки в метод checkPerson, для проверки email **/
    @GetMapping("/checkPerson")
    public String showCheckPersonForm()
    {
        return "shop/checkRegistration"; // Возвращаем представление с формой для ввода email
    }

    /** проверка пользователя по его email на предмет его регистрации **/
    @PostMapping("/checkPerson")
    public String checkPerson(RedirectAttributes redirectAttributes, @RequestParam("email") String email)
    {
        Person currentUser = personDAO.isUserRegistered(email); // Проверяем, зарегистрирован ли пользователь
        if (currentUser != null)
        {
            redirectAttributes.addFlashAttribute("currentUser", currentUser); // Пользователь зарегистрирован, перенаправляем на страницу корзины
            return "redirect:/cart";
        }
        else
        {
            return "redirect:shop/registration"; // Пользователь не зарегистрирован, перенаправляем на страницу регистрации
        }
    }

    /** показать всю корзину **/
    @GetMapping("/cart")
    public String getCart(Model model, @ModelAttribute("currentUser") Person currentUser)
    {
        if (currentUser != null)
        {
            Integer id = currentUser.getId();
            Cart userCart = cartDAO.getCartByUserId(id); // Получаем корзину текущего пользователя
            if (userCart == null)
            {
                userCart = new Cart();            // Если корзина еще не существует, создаем новую корзину
                userCart.setPerson(currentUser); // Связываем корзину с текущим пользователем
                cartDAO.saveCard(userCart);     // Сохраняем созданную корзину в базе данных
            }

            model.addAttribute("userCart", userCart); // Передаем корзину в модель для отображения на странице корзины


            List<Thing> things = thingDAO.getAllThigs();       //Получение списка всех товаров
            model.addAttribute("things", things); // Добавление списка товаров в модель


            model.addAttribute("currentUser", currentUser); // передаём в модель текущего пользователя

            model.addAttribute("currentUserId", currentUser.getId()); // Передаем идентификатор текущего пользователя в модель

        }
        else
        {
            throw new RuntimeException("Пользователь не был передан в данный метод!");
        }
        return "cart/cart"; // Возвращаем представление корзины
    }


    /** метод добавления товара в корзину **/
    @PostMapping("/addThing")
    public String addThing(@RequestParam("selectedThingId") Integer selectedThingId, @ModelAttribute("currentUser") Integer id)
    {
        if (id != null)
        {
            Person currentUser = personDAO.getPersonById(id); // получаем текущего пользователя

            Cart cart = cartDAO.getCartByUserId(id); // Получаем корзину текущего пользователя

            if (cart == null)
            {
                cart = new Cart();
                cart.setPerson(currentUser); // устанавливаем корзине текущего пользователя
                currentUser.setCart(cart); // устанавливаем текущему пользователю корзину

                personDAO.savePersonByCart(currentUser); // Сохранение person
                cartDAO.saveCard(cart); // Сохранение cart

            }
            NewThing selectedThing = thingDAO.getThingById(selectedThingId); // Получаем выбранный товар по его id
            if (cart.getListOfnewThings() == null)
            {
                cart.setListOfnewThings(new ArrayList<>());
            }
            cart.getListOfnewThings().add(selectedThing); // Добавляем выбранный товар в корзину
            cartDAO.addThingToCart(cart, selectedThing); // Сохраняем изменения в корзине

            // Используем merge для обновления состояния объекта Person в базе данных
            //personDAO.savePersonByCart(currentUser);

            // Сохраняем изменения в корзине
            //cartDAO.saveCard(cart);
            return "redirect:/cart"; // Перенаправляем на страницу корзины
        }
        else
        {
            // Пользователь не зарегистрирован, перенаправляем на страницу регистрации или другую страницу, где можно выполнить регистрацию
            return "redirect:/shop/registration";
        }
    }


    /** удаление вещи из корзины по её id **/
    @PostMapping("/removeThing")
    public String removeThing(@RequestParam("thing_id") int thing_id, Model model)
    {
        try
        {
            // Получить текущего пользователя, например, из сессии или извне
           // Person currentUser = ... ; // Получить текущего пользователя, нужно реализовать эту логику

            // Найти корзину текущего пользователя
           // Cart userCart = cartDAO.getCartByUserId(currentUser.getId());

            // Удалить товар из корзины пользователя по его идентификатору
            cartDAO.deleteCartThingFromCart(thing_id);

            // Обновить информацию о корзине
            updateCartInfo(model);
        }
        catch (Exception e)
        {
            // Обработка возможных исключений, например, логирование ошибки
            e.printStackTrace();
        }
        return "redirect:/cart";
    }

//    @PostMapping("/removeThing")
//    public String removeThing(@RequestParam("thing_id") int thing_id, Model model)
//    {
//        try
//        {
//            synchronized (listOfnewThings)
//            {
//                if (thing_id >= 0 && thing_id < listOfnewThings.size())
//                {
//                    // Удаляем вещь из списка listOfnewThings по индексу thing_id
//                    listOfnewThings.remove(thing_id);
//                    // Обновляем информацию о корзине
//                    updateCartInfo(model);
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            // Обработка возможных исключений, например, логирование ошибки
//            e.printStackTrace();
//        }
//        return "redirect:/cart";
//    }


    

    @GetMapping("/add-product")
    public String showAddProductForm(Model model)
    {
        List<Thing> listOfnewThings = thingDAO.getAllThigs();
        model.addAttribute("listOfnewThings", listOfnewThings);
        model.addAttribute("newThing", new NewThing());
        return "add-product-form";
    }

    private void updateCartInfo(Model model)
    {
        model.addAttribute("listOfnewThings", listOfnewThings);
        model.addAttribute("sumOfThingsInOneCart", listOfnewThings.size());
        model.addAttribute("totalCostOfThingsInOneCart", listOfnewThings.stream().mapToInt(NewThing::getThing_price).sum());
    }


}




//    @PostMapping("/addThing")
//    public String addThing(@RequestParam("selectedThingId") Integer selectedThingId, Model model, @ModelAttribute("userCart") Cart userCart, @RequestParam("currentUserId") Integer currentUserId) {
//        // Получаем объект Person по userId из базы данных или из сессии
//        Person currentUser = personDAO.getPersonById(currentUserId);
//
//        if (currentUser != null && currentUser.getId() != null)
//        {
//            // Пользователь уже зарегистрирован, можно добавлять товар в корзину
//            Cart cart = cartDAO.getCartByUserId(currentUser.getId()); // Получаем корзину текущего пользователя
//            if (cart == null)
//            {
//                cart = new Cart();
//                cart.setPerson(currentUser);
//                cartDAO.saveCard(cart); // Сохраняем корзину в базе данных
//            }
//            NewThing selectedThing = thingDAO.getThingById(selectedThingId); // Получаем выбранный товар по его id
//            if (cart.getListOfnewThings() == null)
//            {
//                cart.setListOfnewThings(new ArrayList<>());
//            }
//            cart.getListOfnewThings().add(selectedThing); // Добавляем выбранный товар в корзину
//            cartDAO.addThingToCart(cart, selectedThing); // Сохраняем изменения в корзине
//
//            // Сохраняем изменения в корзине
//            cartDAO.saveCard(userCart);
//
//            // Обновляем объект userCart в модели
//            model.addAttribute("userCart", userCart);
//
//            return "redirect:/cart"; // Перенаправляем на страницу корзины
//        }
//        else
//        {
//            // Пользователь не зарегистрирован, перенаправляем на страницу регистрации или другую страницу, где можно выполнить регистрацию
//            return "redirect:/shop/registration";
//        }
//    }



//    /** метод добавления товара в корзину **/
//    @PostMapping("/addThing")  //работающий метод!
//    public String addThing(@RequestParam("selectedThingId") Integer selectedThingId) {
//    {
//        Cart cart = new Cart(); // Создаем новый объект Cart
//
//        NewThing selectedThing = thingDAO.getThingById(selectedThingId); // Получаем выбранный товар по его id
//
//        if (cart.getListOfnewThings() == null) // Проверяем, что список listOfnewThings не равен null
//        {
//            cart.setListOfnewThings(new ArrayList<>()); // Инициализируем список, если он null
//        }
//
//        cart.getListOfnewThings().add(selectedThing);  // Добавляем выбранный товар в корзину
//
//        cartDAO.addThingToCart(cart, selectedThing); // Сохраняем изменения в корзине
//
//        return "redirect:/cart"; // Перенаправляем на страницу корзины
//    }








// В методе addThing контроллера CartController
//    @PostMapping("/addThing")
//    public String addThing(@RequestParam("selectedThingId") Integer selectedThingId, @RequestParam("userId") Integer userId) {
//        // Получаем объект Person по userId из базы данных или из сессии
//        Person currentUser = personDAO.getPersonById(userId);
//        if (currentUser != null && currentUser.getId() != null)
//        {
//            // Пользователь уже зарегистрирован, можно добавлять товар в корзину
//            Cart cart = cartDAO.getCartByUserId(currentUser.getId()); // Получаем корзину текущего пользователя
//            if (cart == null)
//            {
//                cart = new Cart();
//                cart.setPerson(currentUser);
//                cartDAO.saveCard(cart); // Сохраняем корзину в базе данных
//            }
//            NewThing selectedThing = thingDAO.getThingById(selectedThingId); // Получаем выбранный товар по его id
//            if (cart.getListOfnewThings() == null)
//            {
//                cart.setListOfnewThings(new ArrayList<>());
//            }
//            cart.getListOfnewThings().add(selectedThing); // Добавляем выбранный товар в корзину
//            cartDAO.addThingToCart(cart, selectedThing); // Сохраняем изменения в корзине
//            return "redirect:/cart"; // Перенаправляем на страницу корзины
//        } else {
//            // Пользователь не зарегистрирован, перенаправляем на страницу регистрации или другую страницу, где можно выполнить регистрацию
//            return "redirect:/shop/registration";
//        }
//    }
//


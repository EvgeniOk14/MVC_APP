package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.person.PersonDAO;
import com.example.daocradapi.dao.cart.CartDAO;
import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.cartItem.CartItem;
import com.example.daocradapi.models.person.Person;
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
    //region Fields
    private List<NewThing> listOfnewThings;
    private ThingDAO thingDAO;
    private CartDAO cartDAO;
    private PersonDAO personDAO;
    @PersistenceContext
    private final EntityManager entityManager;
    //endregion

    //region Constructor
    public CartController(List<NewThing> listOfnewThings, ThingDAO thingDAO,
                          CartDAO cartDAO, PersonDAO personDAO,
                          EntityManager entityManager)
    {
        this.listOfnewThings = listOfnewThings;
        this.thingDAO = thingDAO;
        this.cartDAO = cartDAO;
        this.personDAO = personDAO;
        this.entityManager = entityManager;
    }
    //endregion

    /** переход на представление checkRegistration.html для ввода email в форму,
     *  для последующей её отправки в метод checkPerson,
     *  для проверки email
     **/
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
        if (currentUser != null) // если текущий пользователь найден
        {
            redirectAttributes.addFlashAttribute("currentUserId", currentUser.getId()); // Пользователь зарегистрирован, передаём его id в модель
            return "redirect:/cart"; // перенаправляем на страницу корзины
        }
        else
        {
            return "redirect:shop/registration"; // Пользователь не зарегистрирован, перенаправляем на страницу регистрации
        }
    }

    /** показать всю корзину текущего пользователя**/
    @GetMapping("/cart")
    public String getCart(Model model, @ModelAttribute("currentUserId") Integer currentUserId)
    {
        Person currentUser = personDAO.getPersonById(currentUserId); // нахождение текущего пользователя по его id
        if (currentUser != null) // если текущий пользователь не равен нулю (т.е. он найден)
        {
            Cart userCart = cartDAO.getCartByUserId(currentUserId); // Получаем корзину текущего пользователя
            if (userCart == null)                    // Если корзина еще не существует
            {
                userCart = new Cart();             // Если корзина еще не существует, тогда создаем новую корзину
                userCart.setPerson(currentUser);  // Связываем корзину с текущим пользователем
                currentUser.setCart(userCart);   // устанавливаем корзину текущему пользователю
                cartDAO.saveCard(userCart);     // Сохраняем созданную корзину в базе данных
            }
            userCart.setPerson(currentUser);  // Связываем корзину с текущим пользователем
            currentUser.setCart(userCart);   // устанавливаем корзину текущему пользователю
            cartDAO.saveCard(userCart);     // Сохраняем созданную корзину в базе данных

            List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems(); // Получаем список всех товаров из корзины текущего пользователя
            List<Thing> things = thingDAO.getAllThigs(); //Получение списка всех товаров магазина
            /** считаем общую стоимость товаров в корзине текущего пользователя **/
            double totalPrice = cartDAO.calculateTotalPrice(currentUserId); // расчёт общей стоимости товаров в корзине текущего пользователя
            /** считаем общее количество товаров Quantity в корзине текущего пользователя **/
            int totalQuantity = cartDAO.calculateTotalQuantity(currentUserId);

            /** метод добавление параметров в модель (смотри самы крайний метод в этом классе контроллера updateCartInfo): **/
            updateCartInfo(model, listOfCartItemsOfCurrentUser, userCart, things,  currentUser, currentUserId, totalPrice, totalQuantity);
            System.out.println("передаём currentUserId: " + currentUserId);
        }
        else
        {
            throw new RuntimeException("Пользователь не был передан в данный метод!"); // если текущий пользователь не найден или равен null
        }
        return "cart/cart"; // Возвращаем представление корзины
    }


    /** метод добавления товара в корзину текущего пользователя **/
    @PostMapping("/addThing")
    public String addThing(@RequestParam("selectedThingId") Integer selectedThingId,
                           @RequestParam("currentUserId") Integer currentUserId,
                           Model model)
    {
        if (currentUserId != null)                                         // проверяем id текущего пользователя, равно ли оно null?
        {
            Person currentUser = personDAO.getPersonById(currentUserId);  // получаем текущего пользователя

                Cart userCart = cartDAO.getCartByUserId(currentUserId); // Получаем корзину текущего пользователя

                if(userCart == null)
                {
                    Cart newUserCart = new Cart();                       // создаём новую корзину текущему пользователю serCart
                    newUserCart.setPerson(currentUser);                 // устанавливаем корзине текущего пользователя
                    userCart.setListOfCartItems(new ArrayList<>());    // устанавливаем корзине список вещей текущего пользователя
                    currentUser.setCart(newUserCart);                 // устанавливаем текущему пользователю корзину
                    cartDAO.saveCard(newUserCart);                   // Сохранение корзины
                }
                else
                {
                    List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems(); // получаем список вещей в корзине текущего пользователя
                    userCart.setPerson(currentUser);                                            // устанавливаем корзине текущего пользователя
                    userCart.setListOfCartItems(listOfCartItemsOfCurrentUser);                 // устанавливаем корзине список вещей текущего пользователя
                    currentUser.setCart(userCart);                                            // устанавливаем текущему пользователю корзину
                    cartDAO.saveCard(userCart);                                              // Сохранение корзины
                }

            NewThing selectedThing = thingDAO.getThingById(selectedThingId);              // Получаем выбранный товар по его id

            List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems();// получаем список вещей в корзине текущего пользователя

            List<Thing> things = thingDAO.getAllThigs();                              // Получение списка всех товаров из магазина


            /** Добавляем выбранный товар selectedTing в корзину Cart и сохраняем изменения в корзине **/
            cartDAO.addCartItemToCart(userCart, selectedThing);

            /** считаем общую стоимость товаров в корзине текущего пользователя **/
            double totalPrice = cartDAO.calculateTotalPrice(currentUserId);

            /** считаем общее количество товаров Quantity в корзине текущего пользователя **/
            int totalQuantity = cartDAO.calculateTotalQuantity(currentUserId);


            /** метод добавление параметров в модель (смотри самый крайний метод в этом классе контроллера updateCartInfo): **/
            updateCartInfo(model, listOfCartItemsOfCurrentUser, userCart, things,  currentUser, currentUserId,  totalPrice, totalQuantity);

            /** передаём в модель выбранную вещь **/
            model.addAttribute("selectedThing", selectedThing);

            return "cart/cart"; // Перенаправляем на страницу корзины
        }
        else
        {
            return "redirect:/shop/registration"; // Пользователь не зарегистрирован, перенаправляем на страницу регистрации или другую страницу, где можно выполнить регистрацию
        }
    }

    /** удаление вещи из корзины текущего пользователя по её id **/
    @PostMapping("/removeThing")
    public String removeThing(@RequestParam("thing_id") Integer thing_id,
                              @RequestParam("currentUserId") Integer currentUserId,
                              Model model)
    {
        {
            Person currentUser = personDAO.getPersonById(currentUserId); // находим текущего пользователя по его id
            Cart userCart = cartDAO.getCartByUserId(currentUserId);     // находим корзину текущего пользователя

            if (currentUser != null && userCart != null)              // проверяем условие: текущий пользователь не равен нулю и его корзина не равна нулю
            {
                int cart_id = userCart.getId();                     // находим id корзины текущего пользователя
                try
                {
                    cartDAO.removeCartItemFromCartIf(thing_id, cart_id); // удаление товара из корзины

                    List<Thing> things = thingDAO.getAllThigs();       //Получение списка всех товаров

                    List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems(); // получение списка всех товаров в карзине текущего пользователя

                    /** считаем общую стоимость товаров в корзине текущего пользователя **/
                    double totalPrice = cartDAO.calculateTotalPrice(currentUserId);

                    /** считаем общее количество товаров Quantity в корзине текущего пользователя **/
                    int totalQuantity = cartDAO.calculateTotalQuantity(currentUserId);

                    /** метод добавление параметров в модель (смотри самы крайний метод в этом классе контроллера updateCartInfo): **/
                    updateCartInfo(model, listOfCartItemsOfCurrentUser, userCart, things,  currentUser, currentUserId,  totalPrice, totalQuantity);

                    return "cart/cart"; // переходим на представление корзина
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Корзина текущего пользователя не найдена или не установлена");
            }
            return "cart/cart"; // переходим на представление корзина
        }
    }

    /** метод updateCartInfo, добавления параметров в модель, обновление информации **/
    private void updateCartInfo(Model model, List<CartItem> listOfCartItemsOfCurrentUser,
                                Cart userCart, List<Thing> things, Person currentUser,
                                Integer currentUserId, double totalPrice, int totalQuantity)
    {
        /** добавление параметров в модель: **/
        model.addAttribute("listOfCartItemsOfCurrentUser", listOfCartItemsOfCurrentUser); // передаём список всех товаров из корзины текущего пользователя в модель, для передачи их на представление корзины
        model.addAttribute("userCart", userCart); // Передаем корзину в модель для отображения на странице корзины
        model.addAttribute("things", things); // Добавление списка товаров в модель
        model.addAttribute("currentUser", currentUser); // передаём в модель текущего пользователя
        model.addAttribute("currentUserId", currentUserId); // Передаем идентификатор текущего пользователя в модель
        model.addAttribute("totalPrice", totalPrice); // добавление общей стоимости товаров в корзину текущего пользователя
        model.addAttribute("totalQuantity", totalQuantity); // добавление общего количества товаров в корзину текущего пользователя
    }
}

















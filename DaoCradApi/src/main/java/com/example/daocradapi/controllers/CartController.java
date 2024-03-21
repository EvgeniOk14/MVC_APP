package com.example.daocradapi.controllers;

import com.example.daocradapi.dao.cartItem.CartItemDAO;
import com.example.daocradapi.dao.payment.BoughtTingDAO;
import com.example.daocradapi.dao.payment.PaymentDAO;
import com.example.daocradapi.dao.person.PersonDAO;
import com.example.daocradapi.dao.cart.CartDAO;
import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.cartItem.CartItem;
import com.example.daocradapi.models.payments.BoughtThing;
import com.example.daocradapi.models.payments.PaymentCart;
import com.example.daocradapi.models.person.Person;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    //region Fields
    private List<NewThing> listOfnewThings;
    private ThingDAO thingDAO;
    private CartDAO cartDAO;
    private PersonDAO personDAO;
    private PaymentDAO paymentDAO;
    private CartItemDAO cartItemDAO;
    private BoughtTingDAO boughtTingDAO;
    @PersistenceContext
    private final EntityManager entityManager;
    //endregion

    //region Constructor
    public CartController(List<NewThing> listOfnewThings, ThingDAO thingDAO,
                          CartDAO cartDAO, PersonDAO personDAO, PaymentDAO paymentDAO,
                          EntityManager entityManager, CartItemDAO cartItemDAO, BoughtTingDAO boughtTingDAO) {
        this.listOfnewThings = listOfnewThings;
        this.thingDAO = thingDAO;
        this.cartDAO = cartDAO;
        this.personDAO = personDAO;
        this.paymentDAO = paymentDAO;
        this.entityManager = entityManager;
        this.cartItemDAO = cartItemDAO;
        this.boughtTingDAO = boughtTingDAO;
    }
    //endregion

    /**
     * переход на представление checkRegistration.html для ввода email в форму,
     * для последующей её отправки в метод checkPerson,
     * для проверки email
     **/
    @GetMapping("/checkPerson")
    public String showCheckPersonForm() {
        return "shop/checkRegistration"; // Возвращаем представление с формой для ввода email
    }

    /**
     * проверка пользователя по его email на предмет его регистрации
     **/
    @PostMapping("/checkPerson")
    public String checkPerson(RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
        Person currentUser = personDAO.isUserRegistered(email); // Проверяем, зарегистрирован ли пользователь
        if (currentUser != null) // если текущий пользователь найден
        {
            redirectAttributes.addFlashAttribute("currentUserId", currentUser.getId()); // Пользователь зарегистрирован, передаём его id в модель
            return "redirect:/cart"; // перенаправляем на страницу корзины
        } else {
            return "redirect:shop/registration"; // Пользователь не зарегистрирован, перенаправляем на страницу регистрации
        }
    }

    /**
     * показать всю корзину текущего пользователя
     **/
    @GetMapping("/cart")
    public String getCart(Model model, @ModelAttribute("currentUserId") Integer currentUserId) {
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
            List<NewThing> things = thingDAO.getAllThigs(); //Получение списка всех товаров магазина
            Integer cart_id = userCart.getId();  // получаем id корзины текущего пользователя
            System.out.println("!!!---самый первый метод---!!! номер корзины текущего пользователя cart_id = " + cart_id);        // печатем для проверки

            /** считаем общую стоимость товаров в корзине текущего пользователя **/
            double totalPrice = cartDAO.calculateTotalPrice(currentUserId); // расчёт общей стоимости товаров в корзине текущего пользователя

            /** считаем общее количество товаров Quantity в корзине текущего пользователя **/
            int totalQuantity = cartDAO.calculateTotalQuantity(currentUserId);

            /** метод добавление параметров в модель (смотри самы крайний метод в этом классе контроллера updateCartInfo): **/
            updateCartInfo(model, listOfCartItemsOfCurrentUser, userCart, things, currentUser, currentUserId, totalPrice, totalQuantity, cart_id);

        } else {
            throw new RuntimeException("Пользователь не был передан в данный метод!"); // если текущий пользователь не найден или равен null
        }
        return "cart/cart"; // Возвращаем представление корзины
    }


    /**
     * метод добавления товара в корзину текущего пользователя
     **/
    @PostMapping("/addThing")
    public String addThing(@RequestParam("selectedThingId") Integer selectedThingId,
                           @RequestParam("currentUserId") Integer currentUserId,
                           @RequestParam("cart_id") Integer cart_id,
                           Model model) {
        System.out.println("проверка передачи в метод addThing параметра card_id = " + cart_id); // проверка вывода параметра cart_id
        if (currentUserId != null)                                         // проверяем id текущего пользователя, равно ли оно null?
        {
            Person currentUser = personDAO.getPersonById(currentUserId);  // получаем текущего пользователя

            Cart userCart = cartDAO.getCartByUserId(currentUserId); // Получаем корзину текущего пользователя

            if (userCart == null) {
                Cart newUserCart = new Cart();                       // создаём новую корзину текущему пользователю serCart
                newUserCart.setPerson(currentUser);                 // устанавливаем корзине текущего пользователя
                userCart.setListOfCartItems(new ArrayList<>());    // устанавливаем корзине список вещей текущего пользователя
                currentUser.setCart(newUserCart);                 // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(newUserCart);                   // Сохранение корзины
                cart_id = newUserCart.getId();
            } else {
                List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems(); // получаем список вещей в корзине текущего пользователя
                userCart.setPerson(currentUser);                                            // устанавливаем корзине текущего пользователя
                userCart.setListOfCartItems(listOfCartItemsOfCurrentUser);                 // устанавливаем корзине список вещей текущего пользователя
                currentUser.setCart(userCart);                                            // устанавливаем текущему пользователю корзину
                cartDAO.saveCard(userCart);                                              // Сохранение корзины
                cart_id = userCart.getId();
            }

            NewThing selectedThing = thingDAO.getThingById(selectedThingId);              // Получаем выбранный товар по его id

            List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems();// получаем список вещей в корзине текущего пользователя

            List<NewThing> things = thingDAO.getAllThigs();                              // Получение списка всех товаров из магазина


            /** Добавляем выбранный товар selectedTing в корзину Cart и сохраняем изменения в корзине **/
            cartDAO.addCartItemToCart(userCart, selectedThing);

            /** считаем общую стоимость товаров в корзине текущего пользователя **/
            double totalPrice = cartDAO.calculateTotalPrice(currentUserId);

            /** считаем общее количество товаров Quantity в корзине текущего пользователя **/
            int totalQuantity = cartDAO.calculateTotalQuantity(currentUserId);


            /** метод добавление параметров в модель (смотри самый крайний метод в этом классе контроллера updateCartInfo): **/
            updateCartInfo(model, listOfCartItemsOfCurrentUser, userCart, things, currentUser, currentUserId, totalPrice, totalQuantity, cart_id);

            /** передаём в модель выбранную вещь **/
            model.addAttribute("selectedThing", selectedThing);

            return "cart/cart"; // Перенаправляем на страницу корзины
        } else {
            return "redirect:/shop/registration"; // Пользователь не зарегистрирован, перенаправляем на страницу регистрации или другую страницу, где можно выполнить регистрацию
        }
    }

    /**
     * удаление вещи из корзины текущего пользователя по её id
     **/
    @PostMapping("/removeThing")
    public String removeThing(@RequestParam("thing_id") Integer thing_id,
                              @RequestParam("currentUserId") Integer currentUserId,
                              @RequestParam("cart_id") Integer cart_id,
                              Model model) {
        {
            Person currentUser = personDAO.getPersonById(currentUserId); // находим текущего пользователя по его id
            Cart userCart = cartDAO.getCartByUserId(currentUserId);     // находим корзину текущего пользователя

            if (currentUser != null && userCart != null)              // проверяем условие: текущий пользователь не равен нулю и его корзина не равна нулю
            {
                //int cart_id = userCart.getId();                     // находим id корзины текущего пользователя
                try {
                    cartDAO.removeCartItemFromCartIf(thing_id, cart_id); // удаление товара из корзины

                    List<NewThing> things = thingDAO.getAllThigs();       //Получение списка всех товаров

                    List<CartItem> listOfCartItemsOfCurrentUser = userCart.getListOfCartItems(); // получение списка всех товаров в карзине текущего пользователя

                    /** считаем общую стоимость товаров в корзине текущего пользователя **/
                    double totalPrice = cartDAO.calculateTotalPrice(currentUserId);

                    /** считаем общее количество товаров Quantity в корзине текущего пользователя **/
                    int totalQuantity = cartDAO.calculateTotalQuantity(currentUserId);

                    /** метод добавление параметров в модель (смотри самы крайний метод в этом классе контроллера updateCartInfo): **/
                    updateCartInfo(model, listOfCartItemsOfCurrentUser, userCart, things, currentUser, currentUserId, totalPrice, totalQuantity, cart_id);

                    return "cart/cart"; // переходим на представление корзина
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Корзина текущего пользователя не найдена или не установлена");
            }
            return "cart/cart"; // переходим на представление корзина
        }
    }

    /**
     * метод updateCartInfo, добавления параметров в модель, обновление информации
     **/
    private void updateCartInfo(Model model, List<CartItem> listOfCartItemsOfCurrentUser,
                                Cart userCart, List<NewThing> things, Person currentUser,
                                Integer currentUserId, double totalPrice, int totalQuantity, Integer cart_id) {
        /** добавление параметров в модель: **/
        model.addAttribute("listOfCartItemsOfCurrentUser", listOfCartItemsOfCurrentUser); // передаём список всех товаров из корзины текущего пользователя в модель, для передачи их на представление корзины
        model.addAttribute("userCart", userCart); // Передаем корзину в модель для отображения на странице корзины
        model.addAttribute("things", things); // Добавление списка товаров в модель
        model.addAttribute("currentUser", currentUser); // передаём в модель текущего пользователя
        model.addAttribute("currentUserId", currentUserId); // Передаем идентификатор текущего пользователя в модель
        model.addAttribute("totalPrice", totalPrice); // добавление общей стоимости товаров в корзину текущего пользователя
        model.addAttribute("totalQuantity", totalQuantity); // добавление общего количества товаров в корзину текущего пользователя
        model.addAttribute("cart_id", cart_id); // добавили в модель номер корзины текущего пользователя
    }

/**------------------------------------блок карта оплаты-------------------------------------------------------------**/

    /**
     * метод отображения представление addPaymentCartToPerson.html (ввод карты и покупка товара)
     **/
    @GetMapping("/getFormInputCartItems")
    public String getFormInputCartItems(@RequestParam("currentUserId") Integer currentUserId,
                                        @RequestParam("thing_id") Integer thing_id,
                                        @RequestParam("cart_id") Integer cart_id,
                                        Model model) {
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("thing_id", thing_id);
        model.addAttribute("cart_id", cart_id);
        return "payment/addPaymentCartToPerson";
    }

    /** метод ввода данных карты и при успешном нахождении карты -> покупки выбранного товара **/
    @PostMapping("/inputCartItems")
    public String inputCartItems(@RequestParam("currentUserId") Integer currentUserId,
                                 @RequestParam("thing_id") Integer thing_id,
                                 @RequestParam("cardNumber") String cardNumber,
                                 @RequestParam("expirationDate") String expirationDate,
                                 @RequestParam("securityCode") String securityCode,
                                 @RequestParam("cart_id") Integer cart_id,
                                 Model model) {
        Person currentUser = personDAO.getPersonById(currentUserId); // находим текущего пользователя

        NewThing selectedThing = thingDAO.getThingById(thing_id);  // находим выбранную для покупки вещь

        Cart currentUserCart = cartDAO.getCartByUserId(currentUserId); // находим корзину с товарами текущего пользователя
        // Cart cart = cartDAO.getCartById(cart_id);
        System.out.println("вывод карты текущего пользователя = " + currentUserCart);


        List<PaymentCart> listOfPaymentCards = paymentDAO.getPaymentsByUserId(currentUserId);     // получаем список карт
        System.out.println("список карт у пользователя: " + listOfPaymentCards);                  // выводим список карт


/**----------------------------блок проверка карты на валидность, если карта есть то true----------------------------------------------------**/
        if (paymentDAO.valid(cardNumber, expirationDate, securityCode, listOfPaymentCards) != null) // т.е. находит карту card, то покупаем товар данной картой:
        {
            PaymentCart foundPaymentCart = paymentDAO.valid(cardNumber, expirationDate, securityCode, listOfPaymentCards); // находим нужную карту
            System.out.println("валидация прошла успешно! найдена карта: " + foundPaymentCart);

            /**------------------------------------------действия с балансом-------------------------------------------------------------------**/

            if (paymentDAO.checkMoneyInCurrentUserAccount(foundPaymentCart, selectedThing)) // true если денег на карте хватает balance>0 balance>selectedThing.getPrize, то:
            {
                System.out.println("проверка на баланс прошла успешно!");

                Integer balance = foundPaymentCart.getBalance();  // количество денег на счёте (денежный баланс пользователя)

                System.out.println("balance " + balance); // баланс

                int selectedThingPrize = selectedThing.getThing_price();    // цена выбранной вещи

                System.out.println("selectedThingPrize = " + selectedThingPrize); // вывод цены вещи


                int restOfPrizeAfterBuying = balance - selectedThingPrize; //  остаток денег после покупки
                System.out.println("остаток на счёте после покупки = " + restOfPrizeAfterBuying); // вывод остатка на счёте
                foundPaymentCart.setBalance(restOfPrizeAfterBuying);  // устанавливаем новый баланс за вычетом купленной вещи (вещь куплена, деньги списались)


                paymentDAO.updatePayment(foundPaymentCart);         // обновление данных о платёжной карте
                personDAO.update(currentUserId, currentUser);      // обновление пользователя
                cartDAO.updateCard(currentUserCart);              // обновляем корзину

                System.out.println("это новый баланс на карте после покупки = " + foundPaymentCart.getBalance());

                /**-----------------------блок удаление вещи из корзины текущего пользователя-------------------------------------------------------**/

                System.out.println("!!!!!!!!!!!---находимся в блоке удаления вещи из корзины метод InputCart--!!!!!!!!!!!!!!!");
                //PaymentCart foundPaymentCart = paymentDAO.valid(cardNumber,expirationDate, securityCode, currentUserId); // находим нужную карту
                Integer card_id = foundPaymentCart.getId(); // получаем card_id

                System.out.println(" находим номер карты card_id = " + card_id);
                Cart currentCart = cartDAO.getCartById(cart_id);

                List<CartItem> cartItemList  = currentCart.getListOfCartItems();
                System.out.println("текущая корзина пользоавателя: " + currentCart);

                System.out.println("список вещей в корзине у пользователя: " + cartItemList);
                //List<CartItem> cartItemList = cartItemDAO.getCartItemsByCartId(card_id); // получаем список всех вещей в корзине текущего пользователя
                System.out.println("!!!!!!!!!----------список товаров в корзине у пользователя---------!!!!!! " + cartItemList);
                for (CartItem thingItem: cartItemList)  // идём по спску купленных вещей в карте оплаты текущего пользователя
                {
                    System.out.println("находимся в цикле !!!!");
                    if (thingItem.getThing().getThing_id().equals(thing_id) && thingItem.getCartItem_quantity() >= 1) // если id в списке купленных вещей совпадает с id покупаемой вещи и её количество >=1, то:
                    {
                        System.out.println("Количестово до: " + thingItem.getCartItem_quantity());
                        thingItem.setCartItem_quantity(thingItem.getCartItem_quantity() - 1);      // от количества вычитаем единицу
                        cartItemDAO.updateCartItem(thingItem);                                   // обновляем купленную вещь
                        System.out.println("Количестово после: " + thingItem.getCartItem_quantity());
                        if (thingItem.getCartItem_quantity() == 0)                              // если количество вещи в корзине равно нулю, то:
                        {
                            System.out.println("находимся в if количество равно 0 !!!!!");
                            cartItemList.remove(thingItem);                                  // удаляем последнюю вещь из списка купленных вещей в корзине текущего пользователя
                            currentUserCart.setListOfCartItems(cartItemList);               // карте устанавливаем новый список с купленными вещами
                            cartDAO.updateCard(currentUserCart);                           // обновляем карту
                        }
                        cartItemDAO.updateCartItem(thingItem);                           // обновляем вещь в БД


                        cartItemDAO.updateCartItem(thingItem);                    // обновляем карту в БД

                        currentUserCart.setListOfCartItems(cartItemList);      // карте устанавливаем новый список с купленными вещами
                        cartDAO.updateCard(currentUserCart);                  // обновляем карту
                        System.out.println(thingItem.getCartItem_quantity() + " <--- количество уменьшилось на 1");
                        System.out.println(currentUserCart.getListOfCartItems() + " <-- список с вещами после уменьшения на 1");
                        break;
                    }
                }

                /**--------------------------блок удаление вещи из магазина------------------------------------------**/

                System.out.println("находимся в блоке удаления вещи из магазина");
                List<NewThing> listThingsInShop = thingDAO.getAllThigs();
                System.out.println("список всех товаров в магазине: " + listThingsInShop);

                for (NewThing shopThing: listThingsInShop)
                {
                    if(shopThing.getThing_id().equals(thing_id) && shopThing.getQuantity() >=1)
                    {
                        shopThing.setQuantity(shopThing.getQuantity() -1);
                        thingDAO.updateThing(shopThing);
                        break;
                    }
                    else if (shopThing.getThing_id().equals(thing_id) && shopThing.getQuantity() == 0)
                    {
                        System.out.println("данная вещь закончилась на складе!!!");
                        break;
                    }
                }
                //model.addAttribute("card_id", card_id);              // добавляем в модель card_id
                model.addAttribute("currentUserId", currentUserId); // добавляем в модель currentUserId
                model.addAttribute("thing_id", thing_id);          // добавляем в модель thing_id
                model.addAttribute("cart_id", cart_id);
                return "cart/cart"; // переходим к оплате, на представленме chooseCard.tml (выбор карты и оплата)
            }

            else  // null, денег на карте меньше чем покупаемая вещь, добавляем в модель параметры и переходим на страницу сообщения о том что нужно пополнить баланс
            {
                System.out.println("денег на карте меньше чем покупаемая вещь!!!!!!");
                System.out.println("проверяем card_id = " + foundPaymentCart.getId());  // печатаем для проверки card_id

                model.addAttribute("card_id", foundPaymentCart.getId());  // добавляем в модель card_id
                model.addAttribute("currentUserId", currentUserId);      // добавляем в модель currentUserId
                model.addAttribute("thing_id", thing_id);               // добавляем в модель thing_id
                model.addAttribute("cart_id", cart_id);
                System.out.println("сумма меньше покупаемой вещи! перед warning/insufficientBalance");
                return "warning/insufficientBalance"; // переход на страницу с сообщением о пополнении баланса
            }

        }
        /**----------------------------------null не валидно, карты такой нет или список пустой----------------------------------------**/
        else // null, список есть но карту не нашёл, либо и список пустой и карты нет:
        {

            System.out.println("находимся в методе в блоке else foundPaymentCart == null, либо нет списка, либо нет нужной карты");

            /**------------------------------------- логика добавления новой карты: ----------------------------------------------------**/

            if (listOfPaymentCards != null) // если список карт существует, т.е. он не равен нулю, но искомой карты в нём нет
            {
                System.out.println("!!!ЭТО СПИСОК КОТОРЫЙ НЕ ДОЛЖЕН БЫТЬ РАВЕН НУЛЮ!!!!!" + listOfPaymentCards);
                PaymentCart newPaymentCard = new PaymentCart();                    // создаём экземпляр новой карты

                newPaymentCard.setCardNumber(cardNumber);                        // устанавливаем новой карте cardNumber
                newPaymentCard.setExpirationDate(expirationDate);               // устанавливаем новой карте expirationDate
                newPaymentCard.setSecurityCode(securityCode);                  // устанавливаем новой карте securityCode
                newPaymentCard.setPerson(currentUser);                        // устанавливаем новой карте currentUser
                paymentDAO.insertPayment(newPaymentCard);

                BoughtThing boughtThing = new BoughtThing();                 // создаём новый экземпляр купленной вещи
                boughtThing.setThing(selectedThing);                        // устанавливаем купленной вещи покупаемую вещь
                boughtThing.setPaymentCart(newPaymentCard);                // устанавливаем купленной вещи новую карту оплаты
                boughtThing.setBoughtThing_quantity(1);                   // устанавливаем купленной вещи количество равной 1
                boughtTingDAO.insertBoughtThing(boughtThing);            // сохраняем в БД новую созданную купленную вещь

                List<BoughtThing> boughtThingList = new ArrayList<>(); // создаём новый пустой список купленных вещей
                boughtThingList.add(boughtThing);                     // добавляем в список купленных вещей созданную и заполненную купленную вещь

                newPaymentCard.setBoughtThings(boughtThingList);    // устанавливаем новой карте список купленных вещей
                paymentDAO.updatePayment(newPaymentCard);          // сохраняем в БД новую созданную карту оплаты текущему пользователю

                listOfPaymentCards.add(newPaymentCard);          // добавляем в список карт новую созданную карту
                currentUser.setPaymentCarts(listOfPaymentCards);//  устанавливаем текущему пользователю обновлённый список карт
                personDAO.update(currentUserId, currentUser);  //  обновляем  текущего пользователя в БД

                Integer card_id = newPaymentCard.getId();

                model.addAttribute("card_id", card_id);
                model.addAttribute("currentUserId", currentUserId);
                model.addAttribute("thing_id", thing_id);
                model.addAttribute("cart_id", cart_id);
                model.addAttribute("listOfPaymentCards", listOfPaymentCards);

                System.out.println(boughtThing);
                System.out.println(boughtThingList);
                System.out.println(newPaymentCard);
                System.out.println(listOfPaymentCards);
                System.out.println(currentUser);

                System.out.println("прошли весь блок !!!   переброска на выбор карты!!!!");
                //return "payment/chooseCard";  // возврат на представление корзина
                return "warning/insufficientBalance";  // возврат на представление пополнить баланс
            }
            else  // listOfPaymentCards == null, список пустой
            {
                List<PaymentCart> newPaymentCartList = new ArrayList<>();

                PaymentCart newPaymentCard = new PaymentCart();                    // создаём экземпляр новой карты

                newPaymentCard.setCardNumber(cardNumber);                            // устанавливаем новой карте cardNumber
                newPaymentCard.setExpirationDate(expirationDate);                   // устанавливаем новой карте expirationDate
                newPaymentCard.setSecurityCode(securityCode);                      // устанавливаем новой карте securityCode
                newPaymentCard.setPerson(currentUser);                            // устанавливаем новой карте currentUser

                BoughtThing boughtThing = new BoughtThing();                    // создаём новый экземпляр купленной вещи
                boughtThing.setThing(selectedThing);                           // устанавливаем купленной вещи покупаемую вещь
                boughtThing.setPaymentCart(newPaymentCard);                   // устанавливаем купленной вещи новую карту оплаты
                boughtThing.setBoughtThing_quantity(1);                      // устанавливаем купленной вещи количество равной 1

                boughtTingDAO.insertBoughtThing(boughtThing);               // сохраняем в БД новую созданную купленную вещь

                List<BoughtThing> boughtThingList = new ArrayList<>();    // создаём новый пустой список купленных вещей
                boughtThingList.add(boughtThing);                        // добавляем в список купленных вещей созданную и заполненную купленную вещь

                newPaymentCard.setBoughtThings(boughtThingList);       // устанавливаем новой карте список купленных вещей

                paymentDAO.insertPayment(newPaymentCard);            // сохраняем в БД новую созданную карту оплаты текущему пользователю

                newPaymentCartList.add(newPaymentCard);            // добавляем в новый созданный список покупок, вновб созданную и заполненную купленную вещь

                currentUser.setPaymentCarts(newPaymentCartList); // устанавливаем текущему пользователю новый список с купленной вещью
                personDAO.update(currentUserId, currentUser);   // обновляем текущего пользователя в БД

                Integer card_id = newPaymentCard.getId();

                model.addAttribute("card_id", card_id);
                model.addAttribute("listOfPaymentCards", newPaymentCartList);
                model.addAttribute("currentUserId", currentUserId);
                model.addAttribute("thing_id", thing_id);
                model.addAttribute("cart_id", cart_id);
                return "payment/chooseCard";  // возврат на представление корзина
            }
        }
    }


    /** метод покупки товара по зарегистрированной карте (переход с формы представления chooseCart.html) **/
    @PostMapping("/payThing")
    public String buyThing(@RequestParam("thing_id") Integer thing_id,
                           @RequestParam("currentUserId") Integer currentUserId,
                           @RequestParam("card_id") Integer card_id,
                           @RequestParam("cart_id") Integer cart_id,
                           Model model) {
        System.out.println("!!!!!!!!!!!!---------------ЗАШЛИ В МЕТОД   @PostMapping(/buyThing)------------_--!!!!!!!!");

        Person currentUser = personDAO.getPersonById(currentUserId); //получаем текущего пользователя по его id

        //List<PaymentCart> listOfPaymentCards = currentUser.getPaymentCarts(); //лист с картами оплаты
        List<PaymentCart> listOfPaymentCards = paymentDAO.getPaymentsByUserId(currentUserId);     // получаем список карт

        System.out.println("вывод списка  карт текущего пользователя: " + listOfPaymentCards); //!!!!!!!!!!!!

        NewThing selectedThing = thingDAO.getThingById(thing_id);   // получаем выбранную вещь

        PaymentCart validCard = paymentDAO.getPaymentById(card_id); // получаем саму карту оплаты, номер которой пришёл с формы card_id

        Cart currentUserCart = cartDAO.getCartByUserId(currentUserId); // находим корзину с товарами текущего пользователя, по её номеру

        System.out.println("вывод карты текущего пользователя = " + currentUserCart);

        List<CartItem> cartItemList = cartItemDAO.getCartItemsByCartId(cart_id); // получаем список всех вещей в корзине текущего пользователя
        System.out.println("!!!!!!!!!----------список товаров в корзине у пользователя---------!!!!!! " + cartItemList);

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("!!!!!!!!ПРОВЕРКА ПЕРЕДАЧИ ДАННЫХ-----!!! номер выбранной вещи thing_id = " + thing_id);
        System.out.println("!!!!!!!!ПРОВЕРКА ПЕРЕДАЧИ ДАННЫХ-----!!! номер текущего пользователя currentUserId = " + currentUserId);
        System.out.println("!!!!!!!!ПРОВЕРКА ПОЛУЧЕНИЯ ДАННЫХ-----!!! список карт карт card_id = " + card_id);
        System.out.println("!!!!ПРОВЕРКА ПОЛУЧЕНИЯ ДАННЫХ КАРТЫ-----!!!! " + validCard);
        System.out.println("!!!!!!!!ПРОВЕРКА ПОЛУЧЕНИЯ ДАННЫХ-----!!! выбранная вещь selectedThing = " + selectedThing);
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        if(validCard.getBalance() >= 0 && validCard.getBalance() >= selectedThing.getThing_price())
        {
            System.out.println("проверка на баланс прошла успешно!");

            Integer balance = validCard.getBalance();  // количество денег на счёте (денежный баланс пользователя)

            System.out.println("balance " + balance); // баланс

            int selectedThingPrize = selectedThing.getThing_price();    // цена выбранной вещи

            System.out.println("selectedThingPrize = " + selectedThingPrize); // вывод цены вещи


            int restOfPrizeAfterBuying = balance - selectedThingPrize; //  остаток денег после покупки
            System.out.println("остаток на счёте после покупки = " + restOfPrizeAfterBuying); // вывод остатка на счёте
            validCard.setBalance(restOfPrizeAfterBuying);  // устанавливаем новый баланс за вычетом купленной вещи (вещь куплена, деньги списались)

            System.out.println("это новый баланс на карте после покупки = " + validCard.getBalance());
        }

        else  // денег на карте меньше чем покупаемая вещь, добавляем в модель параметры и переходим на страницу сообщения о том что нужно пополнить баланс
        {
            System.out.println("денег на карте меньше чем покупаемая вещь!!!!!!");
            System.out.println("проверяем card_id = " + card_id);              // печатаем для проверки card_id

            model.addAttribute("card_id", card_id);              // добавляем в модель card_id
            model.addAttribute("currentUserId", currentUserId); // добавляем в модель currentUserId
            model.addAttribute("thing_id", thing_id);          // добавляем в модель thing_id
            model.addAttribute("cart_id", cart_id);
            System.out.println("сумма меньше покупаемой вещи! перед warning/insufficientBalance смотрим hing_id = " + thing_id);
            return "warning/insufficientBalance"; // переход на страницу с сообщением о пополнении баланса
        }

        /**-----------------------блок удаление вещи из корзины текущего пользователя--------------------------------------**/

        System.out.println("находимся в блоке удаления вещи из козины метода /payThing");
        System.out.println("Список вещей в корзине: " + cartItemList);
        for (CartItem thingItem: cartItemList)  // идём по спску купленных вещей в карте оплаты текущего пользователя
        {
            if(thingItem.getThing().getThing_id().equals(thing_id) && thingItem.getCartItem_quantity() >= 2) // если id в списке купленных вещей совпадает с id покупаемой вещи и её количество >=1, то:
            {
                System.out.println("номер покупаемой вещи thing_id = " + thing_id);
                System.out.println("номер вещи в списке вещей в корзине: = " + thingItem.getThing().getThing_id());
                System.out.println("Количество вещи до: " + thingItem.getCartItem_quantity());
                thingItem.setCartItem_quantity(thingItem.getCartItem_quantity() -1);      // от количества вычитаем единицу
                System.out.println("Количество вещи после: " + thingItem.getCartItem_quantity());
                cartItemDAO.updateCartItem(thingItem);                                   // обновляем купленную вещь

                if(thingItem.getCartItem_quantity() == 0)                              // если количество вещи в корзине равно нулю, то:
                {
                    System.out.println("находимся в if количество равно 0");
                    cartItemList.remove(thingItem);                                  // удаляем последнюю вещь из списка купленных вещей в корзине текущего пользователя
                    currentUserCart.setListOfCartItems(cartItemList);               // карте устанавливаем новый список с купленными вещами
                    cartDAO.updateCard(currentUserCart);                           // обновляем карту
                }
                cartItemDAO.updateCartItem(thingItem);                           // обновляем вещь в БД


                currentUserCart.setListOfCartItems(cartItemList);             // карте устанавливаем новый список с купленными вещами
                cartDAO.updateCard(currentUserCart);                         // обновляем карту

                System.out.println(thingItem.getCartItem_quantity() + " <--- количество уменьшилось на 1");
                System.out.println(currentUserCart.getListOfCartItems() + " <-- список с вещами после уменьшения на 1");
            }
        }
        /**-----------------------------------------блок удаление вещи из магазина-------------------------------------------------------**/

        List<NewThing> listThingsInShop = thingDAO.getAllThigs();                           // получаем список всех товаров в магазине
        for (NewThing shopThing: listThingsInShop)                                         //  идём по списку вещей в магазине
        {
            if(shopThing.getThing_id().equals(thing_id) && shopThing.getQuantity() >=1)  //    если вещь совпала и её количество >= 1, то:
            {
                shopThing.setQuantity(shopThing.getQuantity() -1);                     //      уменьшаем количество на 1
                thingDAO.updateThing(shopThing);                                      //       обновляем вещь
                break;                                                               //        выход из цикла
            }
            else if (shopThing.getThing_id().equals(thing_id) && shopThing.getQuantity() == 0) // если вещь совпала и её количество равно 0, то:
            {
                System.out.println("данная вещь закончилась на складе!!!");      // пишем комментарий, что вещь закончилась
                break;                                                          //  выход из цикла
            }
        }
        model.addAttribute("card_id", card_id);                  // добавляем в модель card_id
        model.addAttribute("currentUserId", currentUserId);     // добавляем в модель currentUserId
        model.addAttribute("thing_id", thing_id);              // добавляем в модель thing_id
        model.addAttribute("cart_id", cart_id);               //  добавляем в модель cart_id
        return "cart/cart";                                              // возврат на представление корзина
    }



    /** метод показа представления putMoneyOnBalance.html и передача на него параметров **/
    @GetMapping("/putMoneyOnBalance")
    public String putMoneyOnBalance(@RequestParam("currentUserId") Integer currentUserId,
                                    @RequestParam("card_id") Integer card_id,
                                    @RequestParam("thing_id") Integer thing_id,
                                    @RequestParam("cart_id") Integer cart_id,
                                    Model model)
    {
        System.out.println("thing_id = " + thing_id);
        System.out.println("находимся в методе putMoneyOnBalance,проверяем передачу в него currentUserId = " + currentUserId);
        System.out.println("проверка передачи в метод card_id = " + card_id);
        model.addAttribute("thing_id", thing_id);
        model.addAttribute("card_id", card_id);
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("cart_id", cart_id);
        return "/payment/putMoneyOnBalance";
    }

    /**
     * метод позволяет положить деньги на счет карты (пополнить баланс, ввести с формы сумму на счёт карты)
     **/
    @Transactional
    @PostMapping("/putMoneyOnBalance/money")
    public String putMoneyOnBalanceForm(@RequestParam("currentUserId") Integer currentUserId,
                                        @RequestParam("card_id") Integer card_id,
                                        @RequestParam("amount") Integer amount,
                                        @RequestParam("thing_id") Integer thing_id,
                                        @RequestParam("cart_id") Integer cart_id,
                                        Model model)
    {
        System.out.println("находимся в методе putMoneyOnBalance/money ");
        Person currentUser = personDAO.getPersonById(currentUserId);

        System.out.println("thing_id = " + thing_id);
        List<PaymentCart> paymentCarts = paymentDAO.getPaymentsByUserId(currentUserId);
        System.out.println("currentUserId = " + currentUserId);
        System.out.println("paymentCarts = " + paymentCarts);
        System.out.println("amount = " + amount);
        System.out.println("card_id = " + card_id);

        PaymentCart currentUserPaymentCart = paymentDAO.getPaymentById(card_id);
        System.out.println("currentUserPaymentCart = " + currentUserPaymentCart);

        if (currentUserPaymentCart.getBalance() == null)
        {
            System.out.println("баланс равен нулю");

            currentUserPaymentCart.setBalance(amount); // кладём на счёт деньги

            Integer newbalance = currentUserPaymentCart.getBalance();
            System.out.println("показ баланса newBalance = " + newbalance);

            paymentDAO.updatePayment(currentUserPaymentCart);

            model.addAttribute("thing_id", thing_id);
            model.addAttribute("card_id", card_id);
            model.addAttribute("currentUserId", currentUserId);
            model.addAttribute("cart_id", cart_id);
            return "payment/chooseCard";
        }
        else
        {
            Integer oldBalance = currentUserPaymentCart.getBalance();
            System.out.println("старый баланс = " + oldBalance);
            System.out.println("денежная сумма в размере = " + amount);
            currentUserPaymentCart.setBalance(currentUserPaymentCart.getBalance() + amount);
            Integer newBalance = currentUserPaymentCart.getBalance();
            System.out.println("новый баланс = " + newBalance);
            paymentDAO.updatePayment(currentUserPaymentCart);

        }

        model.addAttribute("thing_id", thing_id);
        model.addAttribute("card_id", card_id);
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("cart_id", cart_id);
        return "payment/chooseCard";
    }
}

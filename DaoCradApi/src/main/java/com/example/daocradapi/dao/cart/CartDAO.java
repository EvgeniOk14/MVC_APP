package com.example.daocradapi.dao.cart;

import com.example.daocradapi.dao.person.PersonDAO;
import com.example.daocradapi.dao.things.ThingDAO;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.cartItem.CartItem;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Repository
public class CartDAO
{
    //region Fields
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private ThingDAO thingDAO;
    //endregion


    /** сохраняем корзину **/
    @Transactional
    public void saveCard(Cart cart)
    {
        entityManager.persist(cart);
    }

    /** метод находит корзину пользователя по его id **/
    @Transactional
    public Cart getCartByUserId(Integer userId)
    {
        try {
            return entityManager.createQuery("SELECT c FROM Cart c WHERE c.person.id = :userId", Cart.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        }
        catch (NoResultException e)
        {
            return null; // Возвращаем null, если корзина не найдена
        }
    }

    /** Показ всего списка вещей cartItems из корзины Cart **/
    @Transactional
    public List<CartItem> getAllThingsFromCart()
    {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "SELECT cartItem FROM Cart cart JOIN cart.listOfCartItems cartItem", CartItem.class);
        return query.getResultList();
    }

    /** добавление новой вещи в корзину **/
    @Transactional
    public void addCartItemToCart(Cart cart, NewThing selectedThing)
    {
        if (cart != null) // корзина текущего пользователя не равна null
        {
            System.out.println("------------------------------------------------------------");
            System.out.println("это переданная в метод корзина пользователя: " + cart);
            System.out.println("это переданная выбранная вещь: " + selectedThing);
            List<CartItem> list = cart.getListOfCartItems();
            System.out.println("это список вещей в корзине: " + list);
            System.out.println("==========================================================");

            boolean flag = true;
            if (cart.getListOfCartItems() != null) // если товар уже есть в корзине, то добавляем к товару количество товара = +1
            {
                System.out.println("находимся в состоянии когда список не равен нулю!!!");

                if (selectedThing != null && selectedThing.getQuantity() > 0)
                {
                    System.out.println("находимся в состоянии когда в списке вещей есть хотя бы одна вещь и больше!!!");

                    for (CartItem cartItem : cart.getListOfCartItems())
                    {
                        System.out.println("---------------------------------------------");
                        System.out.println(cartItem.getThing().getThing_id());
                        System.out.println("РАВНО ?????");
                        System.out.println(selectedThing.getThing_id());
                        System.out.println("----------------------------------------------");
                        if ((cartItem.getThing().getThing_id()) == (selectedThing.getThing_id()))
                        {
                            System.out.println("находимся в состоянии когда id вещи в списке равно id добавляемой вещи");
                            System.out.println("cartItem.getThing().getThing_id()" + cartItem.getThing().getThing_id());
                            System.out.println("selectedThing.getThing_id()" + selectedThing.getThing_id());

                            //cartItem.getThing().setQuantity(cartItem.getThing().getQuantity() + 1);
                            cartItem.setCartItem_quantity(cartItem.getCartItem_quantity() + 1);

                            System.out.println("!!После количество товара в корзине quantity: " + cartItem.getCartItem_quantity());
                            System.out.println("!!вещь: " + cartItem);
                            System.out.println("!!список вещей: " + cart.getListOfCartItems());
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag)
                {
                    System.out.println("находимся в состоянии создания вещи в списке корзины cartItem");

                    // Если товара нет в корзине, добавляем его с количеством 1

                    CartItem cartItem = new CartItem();                    // создать новую вещь в списке корзины текущего пользователя
                    cartItem.setCartItem_id(selectedThing.getThing_id()); // установить id создаваемой вещи id выбраной вещи
                    cartItem.setThing(selectedThing);                    // установить созданной вещи, выбранную вещь
                    cartItem.setCart(cart);                             // установить вещи корзину
                    cartItem.setCartItem_quantity(1);                  // устанавливаем созданной вещи количество товаров равное 1
                    //cartItem.getThing().setQuantity(1);               // устанавливаем в сохранённый товар так же количество равное 1

                    cart.getListOfCartItems().add(cartItem);

                    System.out.println("!!!!!!!!!!!! корзина !!!!!! :" + cart);
                    System.out.println("!!!!!список вещей: " + cart.getListOfCartItems());
                    System.out.println("!!!!!вещь: " + cartItem);
                }
            }
            entityManager.merge(cart); // Обновляем корзину в базе данных
            System.out.println("корзина итог: " + cart);
            System.out.println("список вещей в корзине итог: " + cart.getListOfCartItems());
        }
        else
        {
            System.out.println("Корзина или список вещей в корзине равен null.");
        }
    }

    /** получить товар из корзины по его id **/
    @Transactional
    public CartItem getCartItemFromCartById(Integer id)
    {
        return entityManager.find(CartItem.class, id);
    }

    /** получить корзину по её id **/
    @Transactional(readOnly = true)
    public Cart getCartById(Integer cartId)
    {
        return entityManager.find(Cart.class, cartId);
    }

    /** Данный метод удалят товар из таблицы cart_items, по id вещи (thing_id) и id корзины пользователя (cart_id) **/
    @Transactional
    public void removeCartItemFromCart(int thing_id, int cart_id)
    {
        entityManager.createQuery(
                        "DELETE FROM CartItem cartItem WHERE cartItem.thing.thing_id = :thingId")
                .setParameter("thingId", thing_id)
                .executeUpdate();
    }

    /** Данный метод удаляет товар по условию:
     * 1) удаляет количество товара, если количество товара больше 1
     * 2) удаляет сам товар, если количество товара равно 1 (т.е. товар последний)
     * 3) показывает предупреждение: "Данный товар отсутствует в Вашей корзине!", если количество товара меньше 1 (т.е. товар отсутствует в корзине)
     * **/
    @Transactional
    public void removeCartItemFromCartIf(int thing_id, int cart_id)
    {
        // Поиск CartItem по thing_id и cart_id
        CartItem cartItem = entityManager.createQuery(
                        "SELECT ci FROM CartItem ci WHERE ci.thing.thing_id = :thingId", CartItem.class)
                .setParameter("thingId", thing_id)
                .getSingleResult();

        // Если нашли CartItem
        if (cartItem != null)  // если найденный товар не равен нулю, то:
        {
            if (cartItem.getCartItem_quantity() >= 1) // Если количество товара в корзине больше 1, то:
            {
                cartItem.setCartItem_quantity(cartItem.getCartItem_quantity() -1); // уменьшаем его на 1
            }
            if (cartItem.getCartItem_quantity() < 1) // Если количество товара равно 1 (последний товар в корзине), то:
            {
                removeCartItemFromCart(thing_id, cart_id); // удаляем сам товар CartItem
                //entityManager.remove(cartItem); <--- можно также использовать такой метод удаления товара
            }
        }
        else
        {
            throw new RuntimeException("Данный товар отсутствует в Вашей корзине!"); // Если товар не найден, выбрасываем исключение
        }
    }

        /** суммирование общей стоимости в корзине текущего пользователя **/
        public double calculateTotalPrice(Integer currentUserId)
        {
            double totalPrice = 0.0;

            Cart cart = getCartByUserId(currentUserId);
            if (cart != null && cart.getListOfCartItems() != null)
            {
                List<CartItem> listOfCartItemsOfCurrentUser = cart.getListOfCartItems();

                for (CartItem item : listOfCartItemsOfCurrentUser)
                {
                        int thingQuantity = item.getCartItem_quantity();
                    if (item.getThing() != null && thingQuantity != 0)
                    {
                        int cartItemPrice = item.getThing().getThing_price();
                        totalPrice += cartItemPrice * thingQuantity;
                    }
                }
                return totalPrice;
            }
            return totalPrice;
        }

    /** расчёт общего количества товаров в корзине текущего пользователя **/
    public int calculateTotalQuantity(Integer currentUserId)
    {
        int totalQuantity = 0;
        Cart cart = getCartByUserId(currentUserId);
        if (cart != null && cart.getListOfCartItems() != null)
        {
            List<CartItem> listOfCartItemsOfCurrentUser = cart.getListOfCartItems();

            for (CartItem item : listOfCartItemsOfCurrentUser)
            {
                int thingQuantity = item.getCartItem_quantity();
                totalQuantity = totalQuantity + thingQuantity;
            }
            return totalQuantity;
        }
        return totalQuantity;
    }


//    /** суммирование общей стоимости в корзине текущего пользователя способ через Stream Api  **/
//    public double calcSumTotalCost(List<CartItem> listOfThingsOfCurrentUser)
//    {
//        double totalCost = listOfThingsOfCurrentUser.stream().mapToInt(CartItem::getCartItem_price).sum();
//        return totalCost;
//    }

    /** метод редактирование вещи в корзине **/
    @Transactional
    public void editCartThing(Integer id, NewThing editThing)
    {
        if(id != null && editThing != null)
        {
            NewThing existingCartThing = thingDAO.getThingById(id);

            if(editThing != null)
            {
                existingCartThing.setThing_id(editThing.getThing_id());
                existingCartThing.setThing_gender(editThing.getThing_gender());
                existingCartThing.setThing_name(editThing.getThing_name());
                existingCartThing.setThing_size(editThing.getThing_size());
                existingCartThing.setThing_color(editThing.getThing_color());
                existingCartThing.setThing_price(editThing.getThing_price());
            }
            entityManager.merge(existingCartThing);
        }
    }
}




// /** было: **/
//    /** получить товар из корзины по его id **/
//    @Transactional
//    public NewThing getCartThingById(Integer id)
//    {
//        return entityManager.find(NewThing.class, id);
//    }

//    /** добавление новой вещи в корзину **/
//    @Transactional
//    public void addThingToCart(Cart cart, NewThing selectedThing)
//    {
//        boolean flag = true;
//        if(cart.getListOfnewThings() != null) // если товар уже есть в корзине, тодобавляем к товару количество товара = +1
//        {
//            System.out.println("это список вещей в корзине: " + cart.getListOfnewThings());
//            if (selectedThing != null && selectedThing.getQuantity() > 0)
//            {
//                for (NewThing thing : cart.getListOfnewThings())
//                {
//                    if (thing.getThing_id() == (selectedThing.getThing_id()))
//                    {
//                        System.out.println("ДО!!!  quantity до увеличения на 1: "  + thing.getQuantity());
//                        // Если товар уже есть в корзине, увеличиваем его количество
//                        thing.setQuantity(thing.getQuantity() + 1);
//                        flag = false;
//                        break;
//                    }
//                }
//            }
//            if(flag)
//            {
//                // Если товара нет в корзине, добавляем его с количеством 1
//                NewThing newThing = new NewThing();
//                newThing.setThing_id(selectedThing.getThing_id());
//                newThing.setThing_name(selectedThing.getThing_name());
//                newThing.setThing_gender(selectedThing.getThing_gender());
//                newThing.setThing_size(selectedThing.getThing_size());
//                newThing.setThing_color(selectedThing.getThing_color());
//                newThing.setThing_price(selectedThing.getThing_price());
//                newThing.setQuantity(1);
//                cart.getListOfnewThings().add(newThing);
//            }
//        }
//        entityManager.merge(cart); // Обновляем корзину в базе данных
//    }




//    /** Метод для удаления вещи из корзины по её id **/
//    @Transactional
//    public void removeThingFromCart1(int thing_id, int cart_id)
//    {
//        String sql = "DELETE FROM table_list_of_things_in_one_cart WHERE cart_id = " + cart_id + " AND thing_id = " + thing_id;
//        entityManager.createNativeQuery(sql).executeUpdate();
//    }

//    /** альтернативный метод удаления вещи по её количеству **/
//    @Transactional
//    public void removeThingFromCart2(int thing_id, int cart_id)
//    {
//        NewThing findingThing = thingDAO.getThingById(thing_id);
//        Integer quantity = findingThing.getQuantity();
//        if(quantity > 1)
//        {
//            findingThing.setQuantity(quantity-1);
//        }
//        else if (quantity == null && quantity == 1)
//        {
//            String sql = "DELETE FROM table_list_of_things_in_one_cart WHERE cart_id = " + cart_id + " AND thing_id = " + thing_id;
//            entityManager.createNativeQuery(sql).executeUpdate();
//        }
//        else
//        {
//            throw new RuntimeException("товар закончился!");
//        }
//    }





//    /** добавление новой вещи в корзину **/
//    @Transactional
//    public void addThingToCart(Cart cart, NewThing existingThing)
//    {
//        if (existingThing != null)
//        {
//            NewThing newThing = new NewThing();
//            newThing.setThing_id(existingThing.getThing_id());
//            newThing.setThing_name(existingThing.getThing_name());
//            newThing.setThing_gender(existingThing.getThing_gender());
//            newThing.setThing_size(existingThing.getThing_size());
//            newThing.setThing_color(existingThing.getThing_color());
//            newThing.setThing_price(existingThing.getThing_price());
//            newThing.setQuantity(existingThing.getQuantity);
//
//            cart.getListOfnewThings().add(newThing); // Добавляем новую вещь в список в корзине
//            entityManager.merge(cart); // Обновляем корзину в базе данных
//        }
//    }



//    /** суммирование общей стоимости в корзине текущего пользователя **/
//    public double calculateTotalCost(Integer currentUserId) {
//        Cart cart = getCartByUserId(currentUserId);
//        double totalCost = 0.0;
//        if (cart != null && cart.getListOfnewThings() != null)
//        {
//            for (NewThing thing : cart.getListOfnewThings())
//            {
//                totalCost += thing.getThing_price();
//            }
//        }
//        return totalCost;
//    }





//    /** добавление новой вещи в корзину **/
//    @Transactional
//    public int addThingToCart(Cart cart, NewThing selectedThing)
//    {
//        int SumQuantityInCart = 0;
//
//        boolean flag = true;
//        if(cart.getListOfnewThings() != null)
//        {
//            System.out.println("это список вещей в корзине: " + cart.getListOfnewThings());
//            if (selectedThing != null && selectedThing.getQuantity() > 0)
//            {
//                for (NewThing thing : cart.getListOfnewThings())
//                {
//                    if (thing.getThing_id() == (selectedThing.getThing_id()))
//                    {
//                        System.out.println("ДО!!!  quantity до увеличения на 1: "  + thing.getQuantity());
//                        // Если товар уже есть в корзине, увеличиваем его количество
//                        thing.setQuantity(thing.getQuantity() + 1);
//                        SumQuantityInCart = thing.getQuantity();
//                        System.out.println("ПОСЛЕ!!!  quantity после увеличения на 1: " + SumQuantityInCart);
//
//                        flag = false;
//                        break;
//                    }
//                }
//            }
//            if(flag)
//            {
//                // Если товара нет в корзине, добавляем его с количеством 1
//                NewThing newThing = new NewThing();
//                newThing.setThing_id(selectedThing.getThing_id());
//                newThing.setThing_name(selectedThing.getThing_name());
//                newThing.setThing_gender(selectedThing.getThing_gender());
//                newThing.setThing_size(selectedThing.getThing_size());
//                newThing.setThing_color(selectedThing.getThing_color());
//                newThing.setThing_price(selectedThing.getThing_price());
//                newThing.setQuantity(1);
//                SumQuantityInCart = 1;
//                System.out.println("-----------------------------до сохранения-----------------------------------------");
//                System.out.println("количество вещей у вновь созданной вещи: " + newThing.getQuantity());
//                System.out.println("список вещей у текущего пользователя: " + cart.getListOfnewThings());;
//                System.out.println(cart.getListOfnewThings());
//                System.out.println("----------------------------------------------------------------------------------");
//                System.out.println();
//
//                System.out.println("-----------------------------после сохранения-----------------------------------------");
//                cart.getListOfnewThings().add(newThing);
//                System.out.println("количество вещей у вновь созданной вещи после сохранения: " + newThing.getQuantity());
//                System.out.println("список веще у текущего пользователя: после сохранения: " + cart.getListOfnewThings());
//                System.out.println("-------------------------------------------------------------------------------------");
//            }
//        }
//        entityManager.merge(cart); // Обновляем корзину в базе данных
//
//        System.out.println("список вещей в корзине после сохранение в БД: " + cart.getListOfnewThings());
//        System.out.println("суммарное количество товарров в корзине: "  + SumQuantityInCart);
//        return SumQuantityInCart;
//    }
//




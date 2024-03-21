package com.example.daocradapi.dao.cart;

import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.cartItem.CartItem;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@Repository
public class CartDAO
{
    //region Fields
    @PersistenceContext
    private EntityManager entityManager;
    //endregion

    /** сохраняем корзину **/
    @Transactional
    public void saveCard(Cart cart)
    {
        entityManager.persist(cart);
    }
    /** метод обновления корзины **/
    @Transactional
    public void updateCard(Cart cart) {
        entityManager.merge(cart);
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


    /** добавление новой вещи в корзину **/
    @Transactional
    public void addCartItemToCart(Cart cart, NewThing selectedThing)
    {
        if (cart != null) // корзина текущего пользователя не равна null
        {
            boolean flag = true; // устанавливаем флаг true
            if (cart.getListOfCartItems() != null) // если товар уже есть в корзине, то добавляем к товару количество товара = +1
            {
                if (selectedThing != null && selectedThing.getQuantity() > 0)
                {
                    for (CartItem cartItem : cart.getListOfCartItems())
                    {
                        if ((cartItem.getThing().getThing_id()) == (selectedThing.getThing_id())) // сравниваем id товара в корзине с выбранным из списка товаром и если они равны, то:
                        {
                            cartItem.setCartItem_quantity(cartItem.getCartItem_quantity() + 1); // такой товар уже есть в корзине и следовательно добавляем количество товара + 1
                            flag = false; // количество товара добавили, флаг меняем на false
                            break;       // выход
                        }
                    }
                }

                if (flag)  // Если такого товара нет в корзине, добавляем его с количеством 1
                {
                    CartItem cartItem = new CartItem();                    // создать новую вещь в списке корзины текущего пользователя
                    cartItem.setCartItem_id(selectedThing.getThing_id()); // установить id создаваемой вещи id выбраной вещи
                    cartItem.setThing(selectedThing);                    // установить созданной вещи, выбранную вещь
                    cartItem.setCart(cart);                             // установить вещи корзину
                    cartItem.setCartItem_quantity(1);                  // устанавливаем созданной вещи количество товаров равное 1

                    cart.getListOfCartItems().add(cartItem);         // добавляем вновь созданную вещь cartItem в список вещей корзины текущего пользователя
                }
            }
            entityManager.merge(cart);                            // Обновляем корзину в базе данных
        }
        else
        {
            System.out.println("Корзина или список вещей в корзине равен null.");
        }
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
            if (cartItem.getCartItem_quantity() >= 1) // Если количество товара в корзине больше либо равно 1, то:
            {
                cartItem.setCartItem_quantity(cartItem.getCartItem_quantity() -1); // уменьшаем количество товара на 1
            }
            if (cartItem.getCartItem_quantity() < 1) // Если количество товара меньше 1 (последний товар в корзине), то:
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


    /** Показ всего списка вещей cartItems из корзины Cart **/
    @Transactional
    public List<CartItem> getAllThingsFromCart()
    {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "SELECT cartItem FROM Cart cart JOIN cart.listOfCartItems cartItem", CartItem.class);
        return query.getResultList();
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

}

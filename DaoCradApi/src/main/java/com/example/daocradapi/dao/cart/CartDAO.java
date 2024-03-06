package com.example.daocradapi.dao.cart;

import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PersonDAO personDAO;
    //endregion

    /** сохраняем корзину **/
    @Transactional
    public void saveCard(Cart cart)
    {
        entityManager.persist(cart);
    }

    /** метод находит корзину пользователя  **/
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


    /** Показ всего списка вещей из корзины **/
    @Transactional
    public List<NewThing> getAllThingsFromCart()
    {
        TypedQuery<NewThing> query = entityManager.createQuery(
                "SELECT newThing FROM Cart cart " +
                        "JOIN cart.listOfnewThings newThing", NewThing.class);
        return query.getResultList();
    }


    /** добавление новой вещи в корзину **/
    @Transactional
    public void addThingToCart(Cart cart, NewThing existingThing)
    {
        if (existingThing != null)
        {
            NewThing newThing = new NewThing();
            newThing.setThing_id(existingThing.getThing_id());
            newThing.setThing_name(existingThing.getThing_name());
            newThing.setThing_gender(existingThing.getThing_gender());
            newThing.setThing_size(existingThing.getThing_size());
            newThing.setThing_color(existingThing.getThing_color());
            newThing.setThing_price(existingThing.getThing_price());

            cart.getListOfnewThings().add(newThing); // Добавляем новую вещь в список в корзине
            entityManager.merge(cart); // Обновляем корзину в базе данных
        }
    }

    /** получить товар из корзины по его id **/
    @Transactional
    public NewThing getCartThingById(Integer id)
    {
        return entityManager.find(NewThing.class, id);
    }

    /** Метод для удаления вещи из корзины по её id **/
    @Transactional
    public void removeThingFromCart(int thing_id, int cart_id)
    {
        String sql = "DELETE FROM table_list_of_things_in_one_cart WHERE cart_id = " + cart_id + " AND thing_id = " + thing_id;
        entityManager.createNativeQuery(sql).executeUpdate();

    }

    /** суммирование общей стоимости в корзине текущего пользователя **/
    public double calculateTotalCost(Integer currentUserId)
    {
        Cart cart = getCartByUserId(currentUserId);
        double totalCost = 0.0;
        if (cart != null && cart.getListOfnewThings() != null)
        {
            for (NewThing thing : cart.getListOfnewThings())
            {
                totalCost += thing.getThing_price();
            }
        }
        return totalCost;
    }

    /** суммирование общей стоимости в корзине текущего пользователя способ через Stream Api  **/
    public double calcSumTotalCost(List<NewThing> listOfThingsOfCurrentUser)
    {
        double totalCost = listOfThingsOfCurrentUser.stream().mapToInt(NewThing::getThing_price).sum();
        return totalCost;
    }


    /** Получение общего количества товаров во всех корзинах всех зарегистрированных пользователей **/
    @Transactional
    public int getTotalSumOfNewThingsInAllCarts()
    {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT cart FROM Cart cart JOIN FETCH cart.listOfnewThings newThing", Cart.class);

        List<Cart> carts = query.getResultList();
        int totalSum = carts.stream()
                .mapToInt(cart -> cart.getSumThingInCart())
                .sum();
        return totalSum;
    }

    /** метод редактирование вещи в корзине **/
    @Transactional
    public void editCartThing(Integer id, NewThing editThing)
    {
        if(id != null && editThing != null)
        {
            NewThing existingCartThing = getCartThingById(id);

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













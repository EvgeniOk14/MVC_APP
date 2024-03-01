package com.example.daocradapi.dao.cart;

import com.example.daocradapi.dao.PersonDAO;
import com.example.daocradapi.models.Person;
import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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


    /** метод сохраняет или обновляет корзину пользователя **/
    @Transactional
    public void saveOrUpdateCart(Cart cart)
    {
        entityManager.merge(cart);
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

    /** удалить товар из корзины по его id **/
    @Transactional
    public void deleteCartThingFromCart(Integer id)
    {
        NewThing removalItem = getCartThingById(id);
        if (removalItem != null)
        {
            entityManager.remove(removalItem);
        }
    }


    @Transactional
    public void deleteCartThingFromCartFromCurrentUser(Integer thing_id, Integer currentUserId)
    {
        Person currentUser = personDAO.getPersonById(currentUserId);
        Cart currentUserCart = currentUser.getCart();
        List<NewThing> listOfThingsOfCurrentUser = currentUserCart.getListOfnewThings();

        Iterator<NewThing> iterator = listOfThingsOfCurrentUser.iterator();
        while (iterator.hasNext())
        {
            NewThing thing = iterator.next();
            if (thing.getThing_id() == thing_id)
            {
                iterator.remove();
                break;
            }
        }
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

    /** Получение общей суммы покупок во всех корзинах всех зарегистрированных пользователей **/
    @Transactional
    public int getTotalCostInAllCarts()
    {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT cart FROM Cart cart JOIN FETCH cart.listOfnewThings newThing", Cart.class);

        List<Cart> carts = query.getResultList();

        int totalCost = carts.stream()
                .mapToInt(cart -> cart.getTotalCost())
                .sum();

        return totalCost;
    }

    /** Получение общей суммы покупок в одной конкретной корзине
     *  конкретного зарегистрированного пользователя по заданному id **/
    @Transactional
    public int getTotalCostInOneCart(Integer id)
    {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT cart FROM Cart cart JOIN FETCH cart.listOfnewThings newThing WHERE cart.id = :id", Cart.class);
        query.setParameter("id", id);

        Cart cart = query.getSingleResult();

        if (cart != null)
        {
            return cart.getTotalCost();
        }
        return 0;
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


    /** Получение общего количества товаров в одной конкретной корзине
     *  конкретного зарегистрированного пользователя по его id **/
    @Transactional
    public int getTotalSumOfNewThingsInOneCart(Integer id)
    {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT cart FROM Cart cart JOIN FETCH cart.listOfnewThings newThing WHERE cart.id = :id", Cart.class);
        query.setParameter("id", id);

        Cart cart = query.getSingleResult();

        if (cart != null)
        {
            return cart.getSumThingInCart();
        }
        return 0;
    }
}







//    @Transactional
//    public Integer saveCard(Cart cart)
//    {
//        entityManager.persist(cart);
//        return cart.getId();
//    }



//    @Transactional
//    public Cart getCartByUserId(Integer userId) {
//        try {
//            Person person = entityManager.find(Person.class, userId); // Получаем объект Person из базы данных по его идентификатору
//            if (person != null)
//            {
//                Cart cart = entityManager.createQuery("SELECT c FROM Cart c WHERE c.person = :person", Cart.class)
//                        .setParameter("person", person)
//                        .getSingleResult();
//                cart.setPerson(person); // Устанавливаем ассоциацию с объектом Person
//                return cart;
//            }
//            else
//            {
//                return null; // Возвращаем null, если пользователь не найден
//            }
//        }
//        catch (NoResultException e)
//        {
//            return null; // Возвращаем null, если корзина не найдена
//        }
//    }



//    @Transactional
//    public Cart getCart(Person currentUser)
//    {
//        if (currentUser != null && currentUser.getId() != null)
//        {
//            try {
//                // Ищем корзину по текущему пользователю
//                return entityManager.createQuery(
//                                "SELECT c FROM Cart c WHERE c.person = :currentUser", Cart.class)
//                        .setParameter("currentUser", currentUser)
//                        .getSingleResult();
//            }
//            catch (NoResultException e)
//            {
//                // Если корзина не найдена, создаем новую и связываем с текущим пользователем
//                Cart newCart = new Cart();
//                newCart.setPerson(currentUser);
//                saveCard(newCart); // Сохраняем созданную корзину
//                return newCart;
//            }
//        }
//        else
//        {
//            throw new IllegalArgumentException("Текущий пользователь не может быть null");
//        }
//    }

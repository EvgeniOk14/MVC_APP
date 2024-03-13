package com.example.daocradapi.dao.things;

import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.products.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class ThingDAO
{
    //region Fields
    @PersistenceContext
    private EntityManager entityManager;
    //endregion


    /** сохоанение вещи в каталоге (БД) **/
    @Transactional
    public void saveThing(NewThing newThing)
    {
        entityManager.persist(newThing);
    }


    /** Показать все вещи из каталога (БД) **/
    @Transactional
    public List<Thing> getAllThigs()
    {
        TypedQuery<Thing> query = entityManager.createQuery("SELECT t FROM Thing t", Thing.class);
        return query.getResultList();
    }

    /** удаление вещи из каталога (БД) **/
    @Transactional
    public void deleteThing(Integer id)
    {
        NewThing removeThing = entityManager.find(NewThing.class, id);
        if (removeThing != null)
        {
            entityManager.remove(removeThing);
        }
    }

    /** редактирование или обновление вещи в каталоге (БД) **/
    @Transactional
    public void editThing(Integer id, NewThing updatedThing)
    {
        NewThing existingThing = entityManager.find(NewThing.class, id);
        if (existingThing != null)
        {
            existingThing.setThing_name(updatedThing.getThing_name());
            existingThing.setThing_gender(updatedThing.getThing_gender());
            existingThing.setThing_size(updatedThing.getThing_size());
            existingThing.setThing_color(updatedThing.getThing_color());
            existingThing.setThing_price(updatedThing.getThing_price());
            existingThing.setQuantity(updatedThing.getQuantity());
        }
        entityManager.merge(existingThing);
    }

    /** получение вещи из каталога (БД) по её thing_id **/
    @Transactional
    public NewThing getThingById(Integer id)
    {
        return entityManager.find(NewThing.class, id);
    }



    /** выборка всех товаров с одинаковым именем, цветом и полом **/
    @Transactional
    public List<NewThing> getListThinsWithSameNameColorGender()
    {
        TypedQuery<NewThing> query = entityManager.createQuery
                (
                "SELECT t FROM NewThing t WHERE t.thing_gender = 'MALE' " +
                        "AND t.thing_name = 'ManSuit' " +
                        "AND t.thing_color = 'красный'", NewThing.class
                ); // выбираем все товары с одинаковым именем, цветом и полом
        List<NewThing> listOfThingWithSameName = query.getResultList(); // присваиваем данной переменной результат выборки


        List<NewThing> availableThings = new ArrayList<>(); // Создаем новый список для товаров с количеством больше нуля
        for (NewThing thing : listOfThingWithSameName)     // идём по списку
        {
            if (thing.getQuantity() > 0)                 // если количество товара больше нуля (т.е. тов есть в наличии), то:
            {
                availableThings.add(thing);            // добавляем данный товар в список доступных товаров
            }
        }
        return availableThings;                     // возвращаем список доступных товаров
    }

    /** метод устанавливает выбранной вещи количество равное на единицу меньше, чем было **/
    @Transactional
    public void removeQuantity(Integer selectedThingId)
    {
        NewThing thingRemoveQuantity = getThingById(selectedThingId); // находим вещь по её id
        thingRemoveQuantity.setQuantity(thingRemoveQuantity.getQuantity() - 1); // устанавливаем вещи количество равное на единицу меньше, чем было
        entityManager.persist(thingRemoveQuantity); // сохраняем эти изменения
        }


    @Transactional
    public List<NewThing> totalGetListThinsWithSameNameColorGender(String name, String color, Gender gender) {
        TypedQuery<NewThing> query = entityManager.createQuery
                (
                "SELECT t FROM NewThing t WHERE t.thing_gender = :gender " +
                        "AND t.thing_name = :name AND t.thing_color = :color", NewThing.class
                );
        query.setParameter("gender", gender);
        query.setParameter("name", name);
        query.setParameter("color", color);

        List<NewThing> listOfThingWithSameName = query.getResultList();

        List<NewThing> availableThings = new ArrayList<>();
        for (NewThing thing : listOfThingWithSameName) {
            if (thing.getQuantity() > 0) {
                availableThings.add(thing);
            }
        }
        return availableThings;
    }

}


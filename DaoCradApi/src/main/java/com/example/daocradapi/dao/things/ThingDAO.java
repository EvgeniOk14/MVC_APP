package com.example.daocradapi.dao.things;

import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.products.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Component
public class ThingDAO
{
    @PersistenceContext
    private EntityManager entityManager;


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
        }
    }
    /** получение вещи из каталога (БД) по её thing_id **/
    @Transactional
    public NewThing getThingById(Integer id) {
        return entityManager.find(NewThing.class, id);
    }

}


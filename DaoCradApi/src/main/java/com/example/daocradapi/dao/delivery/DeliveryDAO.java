package com.example.daocradapi.dao.delivery;

import com.example.daocradapi.models.delivery.DeliveryForm;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository

public class DeliveryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /** метод сохраняет данные в БД **/
    @Transactional
    public void saveDelivery(DeliveryForm deliveryForm) {
        entityManager.persist(deliveryForm);
    }

    /** метод обновляет данные в БД **/
    @Transactional
    public void updateDelivery(DeliveryForm deliveryForm) {
        entityManager.merge(deliveryForm);
    }
}


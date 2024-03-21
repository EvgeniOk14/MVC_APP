package com.example.daocradapi.dao.payment;

import com.example.daocradapi.models.payments.BoughtThing;
import com.example.daocradapi.models.payments.PaymentCart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class BoughtTingDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    /** метод вставки в таблицу payment_cart новой карты **/
    @Transactional
    public void insertBoughtThing(BoughtThing boughtThing)
    {
        entityManager.persist(boughtThing);
    }

    @Transactional
    public void updateBoughtThing(BoughtThing boughtThing)
    {
        entityManager.merge(boughtThing);
    }

    /**  делаем запрос к базе данных для получения списка купленных товаров, привязанных к указанной карте оплаты **/
    @Transactional
    public List<BoughtThing> getBoughtThingsByPaymentCart(PaymentCart paymentCart)
    {
        return entityManager.createQuery("SELECT bt FROM BoughtThing bt WHERE bt.paymentCart = :paymentCart", BoughtThing.class)
                .setParameter("paymentCart", paymentCart)
                .getResultList();
    }

    /** возвращаем пустой список в случае, если карта оплаты не найдена.
     * Этот подход подходит, когда не требуется делать запрос к базе данных,
     * а просто нужно вернуть пустой список как заглушку в случае, если объект не найден **/
    @Transactional
    public List<BoughtThing> getBoughtThingsByPaymentCartId(Integer paymentCartId)
    {
        PaymentCart paymentCart = entityManager.find(PaymentCart.class, paymentCartId);
        if (paymentCart != null)
        {
            return paymentCart.getBoughtThings();
        }
        else
        {
            return Collections.emptyList(); // Возвращаем пустой список, если карта оплаты не найдена
        }
    }

}

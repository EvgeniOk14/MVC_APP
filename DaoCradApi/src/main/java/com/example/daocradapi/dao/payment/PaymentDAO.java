package com.example.daocradapi.dao.payment;

import com.example.daocradapi.models.payments.PaymentCart;
import com.example.daocradapi.models.person.Person;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentDAO {
    @PersistenceContext
    private EntityManager entityManager;

/**------------------------------------------методы CRUD-------------------------------------------------------------**/

    /**
     * метод вставки в таблицу payment_cart новой карты
     **/
    @Transactional
    public void insertPayment(PaymentCart paymentCart) {
        entityManager.persist(paymentCart);
    }


    /**
     * метод создание записи в таблице payment_cart
     **/
    @Transactional
    public void createPayment(PaymentCart payment) {
        entityManager.persist(payment);
    }

    /**
     * метод получение карточки по её идентификатору
     **/
    @Transactional
    public PaymentCart getPaymentById(Integer id) {
        return entityManager.find(PaymentCart.class, id);
    }

    /**
     * метод обновления информации о карточке
     **/
    @Transactional
    public void updatePayment(PaymentCart payment) {
        entityManager.merge(payment);
    }

    /**
     * метод удаления карточки по идентификатору
     **/
    @Transactional
    public void deletePayment(Integer id) {
        PaymentCart payment = entityManager.find(PaymentCart.class, id);
        if (payment != null) {
            entityManager.remove(payment);
        }
    }

    /**
     * метод получение всех записей из таблицы payment_cart
     **/
    @Transactional
    public List<PaymentCart> getAllPayments() {
        return entityManager.createQuery("SELECT pc FROM PaymentCart pc", PaymentCart.class).getResultList();
    }


/**----------------------------------методы получения всех полей по заданному параметру------------------------------**/

    /**
     * Метод для получения всех записей из таблицы payment_cart по заданному user_id
     **/
    @Transactional
    public List<PaymentCart> getPaymentsByUserId(Integer personId) {
        return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.person.id = :personId", PaymentCart.class)
                .setParameter("personId", personId)
                .getResultList();
    }

    /**
     * Метод для получения всех записей из таблицы payment_cart по заданному thing_id
     **/
    @Transactional
    public List<PaymentCart> getPaymentsByThingId(Integer thingId) {
        return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.thing.id = :thingId", PaymentCart.class)
                .setParameter("thingId", thingId)
                .getResultList();
    }

    /**
     * Метод для получения всех записей из таблицы payment_cart по заданному cardNumber
     **/
    @Transactional
    public List<PaymentCart> getPaymentsByCardNumber(String cardNumber) {
        return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.cardNumber = :cardNumber", PaymentCart.class)
                .setParameter("cardNumber", cardNumber)
                .getResultList();
    }

    /**
     * Метод для получения списка cardNumber по заданному user_id
     **/
    @Transactional
    public List<String> getCardNumbersByUserId(Integer userId) {
        return entityManager.createQuery("SELECT pc.cardNumber FROM PaymentCart pc WHERE pc.user.id = :userId", String.class)
                .setParameter("userId", userId)
                .getResultList();
    }


/**---------------------------------методы получения конкретного поля по заданному параметру------------------------ **/

//    /** Метод для получения карточки по заданному cardNumber **/
//    @Transactional
//    public PaymentCart getPaymentByCardNumber(String cardNumber)
//    {
//        return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.cardNumber = :cardNumber", PaymentCart.class)
//                .setParameter("cardNumber", cardNumber)
//                .getSingleResult();
//    }

    /**
     * Метод для получения карточки по заданному cardNumber
     **/
    @Transactional
    public PaymentCart getPaymentByCardNumber(String cardNumber) {
        try {
            return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.cardNumber = :cardNumber", PaymentCart.class)
                    .setParameter("cardNumber", cardNumber)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            return null; // Возвращаем null, если запись не найдена или найдено более одной записи
        }
    }


    /**
     * Метод для получения карточки по заданной дате истечения
     **/
    @Transactional
    public PaymentCart getPaymentByExpirationDate(String expirationDate) {
        return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.expirationDate = :expirationDate", PaymentCart.class)
                .setParameter("expirationDate", expirationDate)
                .getSingleResult();
    }

    /**
     * Метод для получения карточки по заданному securityCode
     **/
    @Transactional
    public PaymentCart getPaymentBySecurityCode(String securityCode) {
        return entityManager.createQuery("SELECT pc FROM PaymentCart pc WHERE pc.securityCode = :securityCode", PaymentCart.class)
                .setParameter("securityCode", securityCode)
                .getSingleResult();
    }

    /**
     * Метод для получения пользователя Person по карте PaymentCart
     **/
    @Transactional
    public Person getPersonByPaymentCart(PaymentCart paymentCart) {
        return paymentCart.getPerson();
    }

    /**
     * Метод для получения пользователя Person по ID карты
     **/
    @Transactional
    public Person getPersonByPaymentCartId(Integer paymentCartId) {
        PaymentCart paymentCart = entityManager.find(PaymentCart.class, paymentCartId);
        if (paymentCart != null) {
            return paymentCart.getPerson();
        } else {
            return null; // или бросить исключение, если требуется обработка отсутствия карточки
        }
    }


    /**--------------------------------методы валидации карты и реализация платежа -----------------------------------**/


    /** метод валидации данных карты текущего пользователя пришедших из формы представления Payment.html **/
    @Transactional
    public PaymentCart valid(String cardNumber, String expirationDate, String securityCode, List<PaymentCart> listOfPaymentCards) {
        System.out.println("!!!!находимся в методе валидации!!!");

        System.out.println("печатаем список карт listOfPaymentCards: " + listOfPaymentCards);

        if (listOfPaymentCards != null)
        {
            for (PaymentCart card : listOfPaymentCards)
            {
                if (card.getCardNumber().equals(cardNumber) && card.getExpirationDate().equals(expirationDate) && card.getSecurityCode().equals(securityCode)) {
                    System.out.println("Карта с такими данными найдена: " + card);
                    return card; // Возвращаем найденную карту
                }
            }
            System.out.println("Карта с такими данными не найдена");
        }
        else
        {
            System.out.println("Список карт пустой");
        }

        return null; // Если ни одна карта не соответствует указанным параметрам, возвращаем null
    }


    /**
     * метод описывающий реализацию выполнения платежа
     **/
    @Transactional
    public boolean checkMoneyInCurrentUserAccount(PaymentCart validCard, NewThing selectedThing)
    {

        System.out.println("зашли в метод проверки количества денег на карте: ");
        int selectedThingPrize = selectedThing.getThing_price();     //  получаем цену покупаемой вещи
        System.out.println("цена купленной вещи: selectedThingPrize = " + selectedThingPrize);

        if (validCard.getBalance() == null)
        {
            System.out.println("баланс равен нулю!!! false");
            return false;
        }
        else if (validCard.getBalance() > 0 && validCard.getBalance() >= selectedThingPrize) // если денежный баланс на крате пользователя равен или привышает цену покупаемой вещи
        {
            System.out.println("баланс больше нуля и больше цены покупаемой вещи");
            int currentUserBalance = validCard.getBalance();
            System.out.println("баланс карты currentUserBalance = " + currentUserBalance);
            return true; // Вернуть true, если денег на карте хватает для покупки выбранной вещи
        }
        else if(validCard.getBalance() <= selectedThingPrize)
        {
            System.out.println("сравнили но равенство не выполнено! false");
            return false;
        }
        return false;
    }
}



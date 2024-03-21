package com.example.daocradapi.models.payments;

import com.example.daocradapi.models.person.Person;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "payment_cart")
public class PaymentCart
{
    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @OneToMany(mappedBy = "paymentCart", cascade = CascadeType.ALL)
    private List<BoughtThing> boughtThings;
    @Column(name = "cardNumber")
    private String cardNumber;
    @Column(name = "expirationDate")
    private String expirationDate;
    @Column(name = "securityCode")
    private String securityCode;
    @Column(name = "balance")
    private Integer balance;
    //endregion

    //region Constructors
    public PaymentCart(Integer id, Person person, List<BoughtThing> boughtThings, String cardNumber,
                       String expirationDate, String securityCode, Integer balance)
    {
        this.id = id;
        this.person = person;
        this.boughtThings = boughtThings;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
        this.balance = balance;
    }

    public PaymentCart()
    {
        //default constructor
    }
    //endregion

    //region Getters & Setters
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Person getPerson()
    {
        return person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }

    public List<BoughtThing> getBoughtThings()
    {
        return boughtThings;
    }

    public void setBoughtThings(List<BoughtThing> boughtThings)
    {
        this.boughtThings = boughtThings;
    }

    public String getCardNumber()
    {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    //endregion

    @Override
    public String toString() {
        return "PaymentCart{" +
                "id=" + id +
                "person=" + person.getName() +
                ", boughtThings=" + boughtThings +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", securityCode='" + securityCode + '\'' +
                ", balance=" + balance +
                '}';
    }
}


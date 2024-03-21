package com.example.daocradapi.models.payments;

import com.example.daocradapi.models.abstractclases.Thing;
import jakarta.persistence.*;

@Entity
public class BoughtThing
{
    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "payment_cart_id")
    private PaymentCart paymentCart;

    @ManyToOne
    @JoinColumn(name = "thing_id")
    private Thing thing;

    @Column(name = "boughtThing_quantity")
    private int boughtThing_quantity = 1;
    //endregion

    //region Constructors
    public BoughtThing(Integer id, PaymentCart paymentCart, Thing thing, int boughtThing_quantity) {
        this.id = id;
        this.paymentCart = paymentCart;
        this.thing = thing;
        this.boughtThing_quantity = boughtThing_quantity;
    }

    public  BoughtThing()
    {
        // defualt Constructor
    }
    //endregion

    //region Getters & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentCart getPaymentCart() {
        return paymentCart;
    }

    public void setPaymentCart(PaymentCart paymentCart) {
        this.paymentCart = paymentCart;
    }

    public Thing getThing() {
        return thing;
    }
    public void setThing(Thing thing) {
        this.thing = thing;
    }


    public int getBoughtThing_quantity() {
        return boughtThing_quantity;
    }

    public void setBoughtThing_quantity(int boughtThing_quantity) {
        this.boughtThing_quantity = boughtThing_quantity;
    }

    //endregion

    @Override
    public String toString() {
        return "BoughtThing{" +
                "id=" + id +
                ", paymentCart=" + paymentCart.getCardNumber() +
                ", thing=" + thing.getThing_name() +
                ", boughtThing_quantity=" + boughtThing_quantity +
                '}';
    }
}

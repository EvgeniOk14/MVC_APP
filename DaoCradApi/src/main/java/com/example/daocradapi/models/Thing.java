package com.example.daocradapi.models;

import jakarta.persistence.*;

@Entity
@Table(name = "things")
public abstract class Thing
{
    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer thing_id;
    @Column(name = "thing_name")
    public String thing_name;
    @Column(name = "thing_size")
    public int thing_size;
    @Column(name = "thing_color")
    public String thing_color;
    @Column(name = "thing_price")
    public Double thing_price;
    //endregion

    //region Constructor
    public Thing(String thing_name, int thing_size, String thing_color, Double thing_price)
    {
        this.thing_name = thing_name;
        this.thing_size = thing_size;
        this.thing_color = thing_color;
        this.thing_price = thing_price;
    }
    public Thing()
    {
        //default constructor
    }
    //endregion

    //region Getter & Setter
    public Integer getThing_id()
    {
        return thing_id;
    }

    public void setThing_id(Integer thing_id)
    {
        this.thing_id = thing_id;
    }

    public String getThing_name()
    {
        return thing_name;
    }

    public void setThing_name(String thing_name)
    {
        this.thing_name = thing_name;
    }

    public int getThing_size()
    {
        return thing_size;
    }

    public void setThing_size(int thing_size)
    {
        this.thing_size = thing_size;
    }

    public String getThing_color()
    {
        return thing_color;
    }

    public void setThing_color(String thing_color)
    {
        this.thing_color = thing_color;
    }

    public Double getThing_price()
    {
        return thing_price;
    }

    public void setThing_price(Double thing_price)
    {
        this.thing_price = thing_price;
    }
    //endregion
}



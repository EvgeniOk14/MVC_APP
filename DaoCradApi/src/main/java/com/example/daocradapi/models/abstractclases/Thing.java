package com.example.daocradapi.models.abstractclases;

import jakarta.persistence.*;

@Entity
@Table(name = "things")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Thing
{
    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer thing_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "thing_gender")
    private Gender thing_gender;


    @Column(name = "thing_name")
    private String thing_name;

    @Column(name = "thing_size")
    private int thing_size;

    @Column(name = "thing_color")
    private String thing_color;

    @Column(name = "thing_price")
    private int thing_price;
    //endregion

    //region Constructor
    public Thing(Gender thing_gender, String thing_name, int thing_size, String thing_color, int thing_price)
    {
        this.thing_gender = thing_gender;
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

    public Gender getThing_gender()
    {
        return thing_gender;
    }

    public void setThing_gender(Gender thing_gender)
    {
        this.thing_gender = thing_gender;
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

    public int getThing_price()
    {
        return thing_price;
    }

    public void setThing_price(int thing_price)
    {
        this.thing_price = thing_price;
    }
    //endregion

    //region toString
    @Override
    public String toString()
    {
        return "Thing{" +
                "thing_id=" + thing_id +
                ", thing_gender=" + thing_gender +
                ", thing_name='" + thing_name + '\'' +
                ", thing_size=" + thing_size +
                ", thing_color='" + thing_color + '\'' +
                ", thing_price=" + thing_price +
                '}';
    }
    //endregion
}



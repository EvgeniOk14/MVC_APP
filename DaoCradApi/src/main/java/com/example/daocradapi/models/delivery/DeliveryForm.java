package com.example.daocradapi.models.delivery;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "table_delivery")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DeliveryForm
{
    //region Field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "recipientName")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Size(min = 2, max = 100, message = "имя должно содержать от 2 до 100 символов! ")
    private String recipientName;

    @Column(name = "address")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    private String address;
    @Column(name = "city")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    private String city;

    @Column(name = "postIndex")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    private String postIndex;

    @Column(name = "country")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    private String country;

    @Column(name = "phone")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    private String phone;
    //endregion

    //region Constructors
    public DeliveryForm(Integer id, String recipientName, String address, String city, String postIndex, String country, String phone) {
        this.id = id;
        this.recipientName = recipientName;
        this.address = address;
        this.city = city;
        this.postIndex = postIndex;
        this.country = country;
        this.phone = phone;
    }

    public DeliveryForm()
    {
        //default Constructor
    }
    //endregion

    //region Gettrers & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    //endregion
}


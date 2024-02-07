package com.example.daocrud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person
{
    //region fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Size(min = 2, max = 30, message = "имя должно содержать от 2 до 30 символов! ")
    private String name;

    @NotEmpty(message = "Поле не должно быть пустым! ")
    private String surname;


    @NotNull(message = "Графу возраст необходмо заполнить! ") // для типа int применяеться @NotNull
    @Max(value = 300, message = "Возраст не может быть более 300 лет!")
    @Min(value = 0, message = "возраст должен быть больше 0! ")
    private int age;

    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Email(message = "почта должна соответствовать требованиям email! ")
    private String email;

    //endregion

    //region Getter Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //endregion
    //region constructor
    public Person(int id, String name, String surname, int age, String email) {
        this.id = id;
        this.name = name;
        this.surname =  surname;
        this.age = age;
        this.email = email;
    }

    public Person()
    {

    }
    //endregion
}
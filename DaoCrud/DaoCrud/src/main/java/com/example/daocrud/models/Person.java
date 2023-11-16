package com.example.daocrud.models;

public class Person {
    //region fields
    private int id;
    private String name;
    private String surname;
    private int age;
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

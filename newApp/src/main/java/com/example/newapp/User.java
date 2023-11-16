package com.example.newapp;

public class User
{
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String location;
    private String gender;

    public User(String firstName, String lastName, String userName, String password, String location, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.location = location;
        this.gender = gender;
    }

    public User(String firstName, String lastName, String userName, String password, String location) {
        this(firstName, lastName, userName, password, location, null);
    }
    public User(String firstName, String lastName, String userName, String password) {
        this(firstName, lastName, userName, password, null, null);
    }
    public User(String firstName, String lastName, String userName) {
        this(firstName, lastName, userName, null, null, null);
    }
    public User(String firstName, String lastName) {
        this(firstName, lastName, null, null, null, null);
    }
    public User(String firstName) {
        this(firstName, null, null, null, null, null);
    }
    public User() {
        this(null, null, null, null, null, null);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUerName() {
        return userName;
    }

    public void setUerName(String uerName) {
        this.userName = uerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}



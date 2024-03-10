package com.example.daocradapi.models.person;

import com.example.daocradapi.models.messageEntity.MessageEntity;
import com.example.daocradapi.models.cart.Cart;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;

@Entity
@Table(name = "person2")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person
{
    //region fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Size(min = 2, max = 30, message = "имя должно содержать от 2 до 30 символов! ")
    //@Pattern(regexp = "^[A-ZА-Я][a-zа-я]+$", message = "Имя должно начинаться с заглавной буквы!") для первой заглавной буквы
    //@Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно содержать только буквы, только латинские буквы!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно содержать только буквы, как русские, так и латинские!")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Size(min = 2, max = 30, message = "имя должно содержать от 2 до 30 символов! ")
    //@Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно содержать только буквы!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно содержать только буквы как русские, так и латинские!")
    private String surname;

    @Column(name = "age")
    @NotNull(message = "Графу возраст необходмо заполнить! ") // для типа int применяеться @NotNull
    @Max(value = 300, message = "Возраст не может быть более 300 лет!")
    @Min(value = 0, message = "возраст должен быть больше 0! ")
    //@Pattern(regexp = "^[0-9]+$", message = "Возраст должен содержать только цифры!") подходит для поля типа String
    private int age;

    @Column(name = "email")
    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Email(message = "почта должна соответствовать требованиям email! ")
    private String email;

    @Column(name = "messages")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<MessageEntity> messages;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Cart cart;
    //endregion

    //region Constructors

    public Person(Integer id, String name, String surname, int age, String email, List<MessageEntity> messages, Cart cart)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.messages = messages;
        this.cart = cart;
    }
    public Person()
    {
        // default Constructor
    }
    //endregion

    //region Getters && Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    //endregion

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}







//    //region constructor
//    public Person(Integer id, String name, String surname, int age, String email, List<MessageEntity> messages, Cart cart)
//    {
//        this.id = id;
//        this.name = name;
//        this.surname =  surname;
//        this.age = age;
//        this.email = email;
//        this.messages = messages;
//        this.cart = cart;
//    }
//
//    public Person()
//    {
//        //default constructor
//    }
//    //endregion


//    //region Getter Setter
//    public Integer getId()
//    {
//        return id;
//    }
//    public void setId(Integer id)
//    {
//        this.id = id;
//    }
//    public String getName()
//    {
//        return name;
//    }
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//    public String getSurname()
//    {
//        return surname;
//    }
//    public void setSurname(String surname)
//    {
//        this.surname = surname;
//    }
//    public int getAge()
//    {
//        return age;
//    }
//    public void setAge(int age)
//    {
//        this.age = age;
//    }
//    public String getEmail()
//    {
//        return email;
//    }
//    public void setEmail(String email)
//    {
//        this.email = email;
//    }
//    public Cart getCart()
//    {
//        return cart;
//    }
//
//    public void setCart(Cart cart)
//    {
//        this.cart = cart;
//    }
//
//    public List<MessageEntity> getMessages()
//    {
//        return messages;
//    }
//
//    public void setMessages(List<MessageEntity> messages)
//    {
//        this.messages = (messages != null) ? messages : new ArrayList<>();
//    }
//    //endregion
//



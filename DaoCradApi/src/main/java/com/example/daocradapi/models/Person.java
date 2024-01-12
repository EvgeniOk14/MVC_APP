package com.example.daocradapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;

@Entity
@Table(name = "person2")
public class Person
{

    //region fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Size(min = 2, max = 30, message = "имя должно содержать от 2 до 30 символов! ")
    //@Pattern(regexp = "^[A-ZА-Я][a-zа-я]+$", message = "Имя должно начинаться с заглавной буквы!") для первой заглавной буквы
    //@Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно содержать только буквы!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно содержать только буквы!")
    private String name;

    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Size(min = 2, max = 30, message = "имя должно содержать от 2 до 30 символов! ")
    //@Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно содержать только буквы!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно содержать только буквы!")
    private String surname;

    @NotNull(message = "Графу возраст необходмо заполнить! ") // для типа int применяеться @NotNull
    @Max(value = 300, message = "Возраст не может быть более 300 лет!")
    @Min(value = 0, message = "возраст должен быть больше 0! ")
    //@Pattern(regexp = "^[0-9]+$", message = "Возраст должен содержать только цифры!") подходит для поля типа String
    private int age;

    @NotEmpty(message = "Поле не должно быть пустым! ")
    @Email(message = "почта должна соответствовать требованиям email! ")
    private String email;

    //endregion

///** -------------------------------------------------------добавил--------------------------------------------------**/
//    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
//    private List<MessageEntity> messages;
//
//    // Конструкторы, геттеры и сеттеры...
//
//    public List<MessageEntity> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(List<MessageEntity> messages) {
//        this.messages = messages;
//    }
//
///**-----------------------------------------------------------------------------------------------------------------**/
    //region constructor
    public Person(Integer id, String name, String surname, int age, String email)
    {
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


    //region Getter Setter

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getSurname()
    {
        return surname;
    }
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    public int getAge()
    {
        return age;
    }
    public void setAge(int age)
    {
        this.age = age;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    //endregion

}


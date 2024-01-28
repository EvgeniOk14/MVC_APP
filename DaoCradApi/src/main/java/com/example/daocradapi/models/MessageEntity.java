package com.example.daocradapi.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Table(name = "messages")
@Entity
public class MessageEntity
{

    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "person_id", nullable = false)
    private Integer person_id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;

    @Column(name = "theme")
    private String theme;

    @Column(name = "messageDate")
    private LocalDate messageDate;

    @Column(name = "messageContent")
    private String messageContent;
    //endregion

/**------------------------------------------------------добавил----------------------------------------------------**/
    //@ManyToOne
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Person person;

    // Конструкторы, геттеры и сеттеры...

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    /**----------------------------------------------------------------------------------------------------------------**/


    //region Constructors
    public MessageEntity(Integer person_id, String name, String email, String theme, LocalDate messageDate, String messageContent, Person person) {
        this.name = name;
        this.email = email;
        this.theme = theme;
        this.messageDate = messageDate;
        this.messageContent = messageContent;
        this.person_id = person_id;
        this.person= person;
    }

    public MessageEntity()
    {

    }
    //endregion


    //region Getters & Settres

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTheme()
    {
        return theme;
    }

    public void setTheme(String theme)
    {
        this.theme = theme;
    }

    public LocalDate getMessageDate()
    {
        return messageDate;
    }

    public void setMessageDate(LocalDate messageDate)
    {
        this.messageDate = messageDate;
    }

    public String getMessageContent()
    {
        return messageContent;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    //endregion
}

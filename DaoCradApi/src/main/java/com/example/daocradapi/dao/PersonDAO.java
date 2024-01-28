package com.example.daocradapi.dao;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.models.MessageEntity;
import com.example.daocradapi.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
@Component
public class PersonDAO
{
    //region Fields
    private final JdbcTemplate jdbcTemplate;
    private final JdbcPersonRepository jdbcPersonRepository;
    //endregion

    //region Constructor
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, JdbcPersonRepository jdbcPersonRepository)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcPersonRepository = jdbcPersonRepository;
    }
    //endregion


    /** ------------------------------------блок People-------------------------------------------------------------**/

     /** метод возвращает всех людей из БД в представление (форма не используеться!) можно переименовать в getAllPeople() **/
    public List<Person> index()
    {
        String SQL = "SELECT * FROM person2";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class));
    }

    /** метод show(int id) выводит всех пользователей с указанным id **/
    public Person show(Integer id)
    {
        String SQL = "SELECT * FROM person2 WHERE id=?";
        return jdbcTemplate.query(SQL, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    /** метод сохранят всех пользователей (данные приходят с формы!) **/
    public void save(Person person)
    {
        String SQL = "INSERT INTO person2 (name, surname, age, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(SQL, person.getName(), person.getSurname(), person.getAge(), person.getEmail());
    }

    /** метод обновляет всех пользователей **/
    public void update(Integer id, Person updatedPerson)
    {
        String SQL = "UPDATE person2 SET name=?, surname=?, age=?, email=? WHERE id=?";
        jdbcTemplate.update(SQL, updatedPerson.getName(), updatedPerson.getSurname(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    /**
     * метод удаляет пользователя по  его id
     **/
    public void delete(Integer id)
    {
        String SQL = "DELETE FROM person2 WHERE id=?";
        jdbcTemplate.update(SQL, id);
    }



    /**----------------------------------------блок message--------------------------------------------------------**/

    public boolean searchBySername(String surname, String name)
    {
        String queryString = "SELECT * FROM person2 WHERE surname = ? AND name = ?";
        List<Person> people = jdbcTemplate.query(queryString, new Object[]{surname, name}, new BeanPropertyRowMapper<>(Person.class));

        if (!people.isEmpty())
        {
            System.out.println("Пользователь с такой фамилией: " + surname + " и именем: " + name + " найден!");
        }
        else
        {
            System.out.println("Пользователь с такой фамилией: " + surname + " и именем: " + name + " не найден!");
        }

        return !people.isEmpty();
    }

    /** нахождение человека по его почте! **/
    public Person searchPersonByEmail1(String email)
    {
        String queryString = "SELECT * FROM person2 WHERE email = ?";
        List<Person> people = jdbcTemplate.query(queryString, new Object[]{email}, new BeanPropertyRowMapper<>(Person.class));

        if (!people.isEmpty())
        {
            System.out.println("Пользователь с почтой: " + email + " найден! Это: " +  people.get(0));
        }
        else
        {
            System.out.println("Пользователь с почтой: " + email + " не найден!");
        }

        return people.get(0);
    }


    public Person getPersonByEmail(String email)
    {
        String queryString = "SELECT * FROM person2 WHERE email = ?";
        try {
            System.out.println(jdbcTemplate.queryForObject(queryString, new Object[]{email}, new BeanPropertyRowMapper<>(Person.class)));
            return jdbcTemplate.queryForObject(queryString, new Object[]{email}, new BeanPropertyRowMapper<>(Person.class));
            }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public Person getPersonById(Integer id)
    {
        String queryString = "SELECT * FROM person2 WHERE id = ?";
        List<Person> people = jdbcTemplate.query(queryString, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class));

        if (!people.isEmpty())
        {
           Person findPerson = people.get(0);
           return findPerson;
        }
        else
        {
            return null;
        }

    }

}
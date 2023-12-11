package com.example.daocradapi.dao;

import com.example.daocradapi.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
@Component
public class PersonDAO
{
    private final JdbcTemplate jdbcTemplate;

    //region Constructor
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
    //endregion


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
        jdbcTemplate.update(SQL,  person.getName(), person.getSurname(), person.getAge(), person.getEmail());
    }

    /** метод обновляет всех пользователей **/
    public void update(Integer id, Person updatedPerson)
    {
        String SQL = "UPDATE person2 SET name=?, surname=?, age=?, email=? WHERE id=?";
        jdbcTemplate.update(SQL, updatedPerson.getName(), updatedPerson.getSurname(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    /** метод удаляет пользователя по  его id **/
    public void delete(Integer id)
    {
        String SQL = "DELETE FROM person2 WHERE id=?";
        jdbcTemplate.update(SQL, id);
    }
}


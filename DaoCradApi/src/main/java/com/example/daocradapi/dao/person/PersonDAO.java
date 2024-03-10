package com.example.daocradapi.dao.person;

import com.example.daocradapi.models.cart.Cart;
import com.example.daocradapi.repositories.JdbcPersonRepository;
import com.example.daocradapi.models.person.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Component
public class PersonDAO
{
    //region Fields
    private final JdbcTemplate jdbcTemplate;
    private final JdbcPersonRepository jdbcPersonRepository;

    @PersistenceContext
    private final EntityManager entityManager;
    //endregion

    //region Constructor
    public PersonDAO(JdbcTemplate jdbcTemplate, JdbcPersonRepository jdbcPersonRepository, EntityManager entityManager)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcPersonRepository = jdbcPersonRepository;
        this.entityManager = entityManager;
    }
    //endregion


    /** проверяем по email, зарегистрирован ли пользователь, перед тем, как работать с корзиной покупок **/
    @Transactional
    public Person isUserRegistered(String email)
    {
        Person currentPerson = searchPersonByEmail1(email); // Проверяем наличие пользователя в базе данных по его адресу электронной почты
        if(currentPerson!=null)
            {
                return currentPerson; // Возвращает person если пользователь найден
            }
        else
            {
                return null; // возвращает null, если пользователь не найден
            }
    }

    /** метод возвращает всех людей из БД в представление (форма не используеться!) можно переименовать в getAllPeople() **/
    @Transactional
    public List<Person> index()
    {
        String SQL = "SELECT * FROM person2";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class));
    }

    /** метод show(int id) выводит всех пользователей с указанным id **/
    @Transactional
    public Person show(Integer id)
    {
        String SQL = "SELECT * FROM person2 WHERE id=?";
        return jdbcTemplate.query(SQL, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    /** метод сохранят всех пользователей (данные приходят с формы!)  **/
    @Transactional
    public void save(Person person)
    {
        String SQL = "INSERT INTO person2 (name, surname, age, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(SQL, person.getName(), person.getSurname(), person.getAge(), person.getEmail());
    }

    /** метод сохранения пользователя, переданного, или найденного по id,
     * создан на всякий случай, пока не используеться,
     *  т.к. в методе addThing()  контроллера CartController
     * пользователь берётся из базы данных и его сохранение не требуется **/
    @Transactional
    public void savePerson(Person person)
    {
        entityManager.persist(person);
    }

    /** метод обновляет всех пользователей **/
    @Transactional
    public void update(Integer id, Person updatedPerson)
    {
        String SQL = "UPDATE person2 SET name=?, surname=?, age=?, email=? WHERE id=?";
        jdbcTemplate.update(SQL, updatedPerson.getName(), updatedPerson.getSurname(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    /**
     * метод удаляет пользователя по  его id
     **/
    @Transactional
    public void delete(Integer id)
    {
        String SQL = "DELETE FROM person2 WHERE id=?";
        jdbcTemplate.update(SQL, id);
    }

    /** нахождение человека по его почте! **/
    @Transactional
    public Person searchPersonByEmail1(String email)
    {
        String queryString = "SELECT * FROM person2 WHERE email = ?";
        List<Person> people = jdbcTemplate.query(queryString, new Object[]{email}, new BeanPropertyRowMapper<>(Person.class));

        if (!people.isEmpty())
        {
            System.out.println("Пользователь с почтой: " + email + " найден! Это: " +  people.get(0));
            return people.get(0);
        }
        else
        {
            System.out.println("Пользователь с почтой: " + email + " не найден!");
            return null;
        }
    }

    /** поиск пользователя по его id **/
    @Transactional
    public Person getPersonById(Integer id)
    {
        System.out.println(id);

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

    /** второй способ поиска пользователя по его email
     * не используется **/
    @Transactional
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

    /** поиск пользователя по его фамилии
     * не используеться **/
    @Transactional
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
}


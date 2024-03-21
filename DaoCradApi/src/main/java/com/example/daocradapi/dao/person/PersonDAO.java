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
        String SQL = "SELECT * FROM person";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class));
    }

    /** метод show(int id) выводит всех пользователей с указанным id **/
    @Transactional
    public Person show(Integer id)
    {
        String SQL = "SELECT * FROM person WHERE id=?";
        return jdbcTemplate.query(SQL, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    /** метод сохранят всех пользователей (данные приходят с формы!)  **/
    @Transactional
    public void save(Person person)
    {
            String SQL = "INSERT INTO person (name, surname, age, email) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(SQL, person.getName(), person.getSurname(), person.getAge(), person.getEmail());

    }
    /** метод обновляет всех пользователей **/
    @Transactional
    public void update(Integer id, Person updatedPerson)
    {
        String SQL = "UPDATE person SET name=?, surname=?, age=?, email=? WHERE id=?";
        jdbcTemplate.update(SQL, updatedPerson.getName(), updatedPerson.getSurname(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }


//    @Transactional
//    public void saveEntity(Person person) {
//        entityManager.persist(person); // Сохраняем пользователя и связанный с ним аккаунт, если он был создан
//    }
//
//    @Transactional
//    public void updateEntity(Person updatedPerson)
//    {
//        entityManager.merge(updatedPerson); // Обновляем пользователя
//    }

    /**
     * метод удаляет пользователя по  его id
     **/
    @Transactional
    public void delete(Integer id)
    {
        String SQL = "DELETE FROM person WHERE id=?";
        jdbcTemplate.update(SQL, id);
    }

    /** нахождение человека по его почте! **/
    @Transactional
    public Person searchPersonByEmail1(String email)
    {
        String queryString = "SELECT * FROM person WHERE email = ?";
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

        String queryString = "SELECT * FROM person WHERE id = ?";
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
        String queryString = "SELECT * FROM person WHERE email = ?";
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
        String queryString = "SELECT * FROM person WHERE surname = ? AND name = ?";
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

    /** метод получает id корзины по номеру пользователя **/
    @Transactional
    public Integer getCurrentUserCartIdByUserId(Integer userId) {
        String queryString = "SELECT c.id FROM table_carts c WHERE c.user_id = ?";
        try {
            return jdbcTemplate.queryForObject(queryString, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    /** метод получает корзину текущего польщователя по номеру пользователя **/
    @Transactional
    public Cart getCurrentUserCartByUserId(Integer userId)
    {
        String queryString = "SELECT cart FROM Person person JOIN person.cart cart WHERE person.id = ?";
        try
        {
            return jdbcTemplate.queryForObject(queryString, Cart.class, userId);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }



}


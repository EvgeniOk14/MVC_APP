package com.example.daocrud.dao;

import com.example.daocrud.JdbcPersonRepository;
import com.example.daocrud.models.Person;
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
        String SQL = "SELECT * FROM person";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class));
    }

    /** метод show(int id) выводит всех пользователей с указанным id **/
    public Person show(Integer id)
    {
        String SQL = "SELECT * FROM person WHERE id=?";
        return jdbcTemplate.query(SQL, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    /** метод сохранят всех пользователей (данные приходят с формы!) **/
    public void save(Person person)
    {
        String SQL = "INSERT INTO person (name, surname, age, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(SQL, person.getName(), person.getSurname(), person.getAge(), person.getEmail());
    }

    /** метод обновляет всех пользователей **/
    public void update(Integer id, Person updatedPerson)
    {
        String SQL = "UPDATE person SET name=?, surname=?, age=?, email=? WHERE id=?";
        jdbcTemplate.update(SQL, updatedPerson.getName(), updatedPerson.getSurname(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    /**
     * метод удаляет пользователя по  его id
     **/
    public void delete(Integer id)
    {
        String SQL = "DELETE FROM person WHERE id=?";
        jdbcTemplate.update(SQL, id);
    }



    /**----------------------------------------блок message--------------------------------------------------------**/

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

    /** нахождение человека по его почте! **/
    public Person searchPersonByEmail1(String email)
    {
        String queryString = "SELECT * FROM person WHERE email = ?";
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

    public Person getPersonById(Integer id)
    {
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

}


//import com.example.daocrud.models.Person;
//import org.springframework.stereotype.Component;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class PersonDAO
//{
//    private static int PEOPLE_COUNT;
//    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
//    private static final String USERNAME = "postgres";
//    private static final String PASSWORD = "oew";
//    private static Connection connection;
//
//    static
//    {
//        try
//        {
//            Class.forName("org.postgresql.Driver");
//        }
//        catch (ClassNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        }
//        catch (SQLException throwables)
//        {
//            throwables.printStackTrace();
//        }
//    }
//
//    /** метод возвращает всех людей из БД в представление (форма не используеться!) **/
//    public List<Person> index()
//    {
//        List<Person> people = new ArrayList<>();
//
//        try
//        {
//            Statement statement = connection.createStatement();
//            String SQL = "SELECT * FROM Person";
//            ResultSet resultSet = statement.executeQuery(SQL);
//
//            while (resultSet.next())
//            {
//                Person person = new Person();
//                person.setId(resultSet.getInt("id"));
//                person.setName(resultSet.getString("name"));
//                person.setSurname(resultSet.getString("surname"));
//                person.setAge(resultSet.getInt("age"));
//                person.setEmail(resultSet.getString("email"));
//
//                people.add(person);
//            }
//        }
//        catch (SQLException throwables)
//        {
//            throwables.printStackTrace();
//        }
//
//        return people;
//    }
//
//    /** метод show(int id) выводит всех пользователей с указанным id **/
//    public Person show(int id)
//    {
//        Person person = null;
//        try
//        {
//            String SQL =  "SELECT * FROM Person WHERE id=?";
//            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            resultSet.next(); // временно!!!
//
//            person = new Person();
//
//            person.setId(resultSet.getInt("id"));
//            person.setName(resultSet.getString("name"));
//            person.setSurname(resultSet.getString("surname"));
//            person.setAge(resultSet.getInt("age"));
//            person.setEmail(resultSet.getString("email"));
//        }
//        catch (SQLException throwables)
//        {
//            throwables.printStackTrace();
//        }
//        return person;
//    }
//
//    /** метод сохранят всех пользователей (данные приходят с формы!) **/
//    public void save(Person person)
//    {
//        try
//        {
//            String SQL = "INSERT INTO Person (name, surname, age, email) VALUES (?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
//            preparedStatement.setString(1, person.getName());
//            preparedStatement.setString(2, person.getSurname());
//            preparedStatement.setInt(3, person.getAge());
//            preparedStatement.setString(4, person.getEmail());
//
//            preparedStatement.executeUpdate();
//        }
//
//        catch (SQLException throwables)
//        {
//            throwables.printStackTrace();
//        }
//    }
//
//    /** метод обновляет всех пользователей **/
//    public void update(int id, Person updatedPerson)
//    {
//        try
//        {
//            String SQL = "UPDATE Person SET name=?, surname=?, age=?, email=? WHERE id=?";
//            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
//            preparedStatement.setString(1, updatedPerson.getName());
//            preparedStatement.setString(2, updatedPerson.getSurname());
//            preparedStatement.setInt(3, updatedPerson.getAge());
//            preparedStatement.setString(4, updatedPerson.getEmail());
//            preparedStatement.setInt(5, id);
//
//            preparedStatement.executeUpdate();
//        }
//        catch (SQLException throwables)
//        {
//            throwables.printStackTrace();
//        }
//    }
//
//    /** метод удаляет пользователя по  его id **/
//    public void delete(int id)
//    {
//        PreparedStatement preparedStatement = null;
//        try
//        {
//            String SQL = "DELETE FROM Person WHERE id=?";
//            preparedStatement = connection.prepareStatement(SQL);
//            preparedStatement.setInt(1, id);
//            preparedStatement.executeUpdate();
//
//        }
//        catch (SQLException throwables)
//        {
//            throwables.printStackTrace();
//        }
//    }
//}
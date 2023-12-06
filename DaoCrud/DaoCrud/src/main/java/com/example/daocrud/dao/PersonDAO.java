package com.example.daocrud.dao;

import com.example.daocrud.models.Person;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO
{
    private static int PEOPLE_COUNT;
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "oew";
    private static Connection connection;

    static
    {
        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /**
     * метод возвращает всех людей из БД в представление (форма не используеться!)
     **/
    public List<Person> index()
    {
        List<Person> people = new ArrayList<>();

        try
        {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next())
            {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return people;
    }

    /** метод show(int id) выводит всех пользователей с указанным id **/
    public Person show(int id)
    {
        Person person = null;
        try
        {
            String SQL =  "SELECT * FROM Person WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next(); // временно!!!

            person = new Person();

            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setSurname(resultSet.getString("surname"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return person;
        // return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    /** метод сохранят всех пользователей (данные приходят с формы!) **/
    public void save(Person person)
    {
            try
            {
                String SQL = "INSERT INTO Person (id, name, surname, age, email) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);

                preparedStatement.setInt(1, person.getId());  // или любой другой способ генерации ID
                preparedStatement.setString(2, person.getName());
                preparedStatement.setString(3, person.getSurname());
                preparedStatement.setInt(4, person.getAge());
                preparedStatement.setString(5, person.getEmail());

                preparedStatement.executeUpdate();
            }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /** метод обновляет всех пользователей **/
    public void update(int id, Person updatedPerson)
    {
//            Person personToBeUdated = show(id);
//            personToBeUdated.setName(updatedPerson.getName());
//            personToBeUdated.setSurname(updatedPerson.getSurname());
//            personToBeUdated.setAge(updatedPerson.getAge());
//            personToBeUdated.setEmail(updatedPerson.getEmail());

        try
        {
            String SQL = "UPDATE Person SET name=?, surname=?, age=?, email=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setString(2, updatedPerson.getSurname());
            preparedStatement.setInt(3, updatedPerson.getAge());
            preparedStatement.setString(4, updatedPerson.getEmail());
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    /** метод удаляет пользователя по  его id **/
    public void delete(int id)
    {
        PreparedStatement preparedStatement = null;
//        people.removeIf(person -> person.getId() == id);
        try {
            String SQL = "DELETE FROM Person WHERE id=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}

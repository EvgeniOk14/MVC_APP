package com.example.daocradapi.dao;

import com.example.daocradapi.models.Person;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PersonMapper implements RowMapper<Person>
{
    @Override
    public Person mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setSurname(resultSet.getString("surname"));
        person.setAge(resultSet.getInt("age"));
        person.setEmail(resultSet.getString("email"));
        return person;
    }
}

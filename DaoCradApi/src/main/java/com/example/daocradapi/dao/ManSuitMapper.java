package com.example.daocradapi.dao;

import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.products.ManSuit;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Component
public class ManSuitMapper implements RowMapper<Thing>
{
    @Override
    public Thing mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        Thing manSuit = new ManSuit();
        manSuit.setThing_id(resultSet.getInt("thing_id"));
        manSuit.setThing_gender(Gender.valueOf(resultSet.getString("thing_gender")));
        manSuit.setThing_name(resultSet.getString("thing_name"));
        manSuit.setThing_size(resultSet.getInt("thing_size"));
        manSuit.setThing_color(resultSet.getString("thing_color"));
        manSuit.setThing_price(resultSet.getInt("thing_price"));
        return manSuit;
    }
}

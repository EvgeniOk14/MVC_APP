package com.example.daocradapi.dao;

import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.products.WomanSuit;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WomanSuitMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        WomanSuit womanSuit = new WomanSuit();
        womanSuit.setThing_id(resultSet.getInt("thing_id"));
        womanSuit.setThing_gender(Gender.valueOf(resultSet.getString("thing_gender")));
        womanSuit.setThing_name(resultSet.getString("thing_name"));
        womanSuit.setThing_size(resultSet.getInt("thing_size"));
        womanSuit.setThing_color(resultSet.getString("thing_color"));
        womanSuit.setThing_price(resultSet.getInt("thing_price"));
        return womanSuit;
    }
}

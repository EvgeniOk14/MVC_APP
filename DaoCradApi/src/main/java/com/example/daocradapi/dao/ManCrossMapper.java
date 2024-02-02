package com.example.daocradapi.dao;

import com.example.daocradapi.models.abstractclases.ClothingType;
import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.products.ManCross;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManCrossMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        ManCross manCross = new ManCross();
        manCross.setThing_id(resultSet.getInt("thing_id"));
        manCross.setThing_gender(Gender.valueOf(resultSet.getString("thing_gender")));
        manCross.setThing_name(resultSet.getString("thing_name"));
        manCross.setThing_size(resultSet.getInt("thing_size"));
        manCross.setThing_color(resultSet.getString("thing_color"));
        manCross.setThing_price(resultSet.getInt("thing_price"));
        return manCross;
    }
}

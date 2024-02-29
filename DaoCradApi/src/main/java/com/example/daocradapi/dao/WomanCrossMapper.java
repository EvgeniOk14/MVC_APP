package com.example.daocradapi.dao;

import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.products.WomanCross;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WomanCrossMapper implements RowMapper
{
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        WomanCross womanCross = new WomanCross();
        womanCross.setThing_id(resultSet.getInt("thing_id"));
        womanCross.setThing_gender(Gender.valueOf(resultSet.getString("thing_gender")));
        womanCross.setThing_name(resultSet.getString("thing_name"));
        womanCross.setThing_size(resultSet.getInt("thing_size"));
        womanCross.setThing_color(resultSet.getString("thing_color"));
        womanCross.setThing_price(resultSet.getInt("thing_price"));
        return womanCross;
    }
}

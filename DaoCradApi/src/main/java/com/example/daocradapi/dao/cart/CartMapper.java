package com.example.daocradapi.dao.cart;

import com.example.daocradapi.dao.PersonMapper;
import com.example.daocradapi.models.Person;
import com.example.daocradapi.models.cart.Cart;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Component
public class CartMapper implements RowMapper<Cart>
{
    private final PersonMapper personMapper;

    public CartMapper(PersonMapper personMapper)
    {
        this.personMapper = personMapper;
    }

    @Override
    public Cart mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        Cart cart = new Cart();
        cart.setId(resultSet.getInt("id"));
        Person person = personMapper.mapRow(resultSet, rowNum);
        cart.setPerson(person);
        return cart;
    }
}

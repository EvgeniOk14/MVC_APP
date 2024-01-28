package com.example.daocradapi.dao;

import com.example.daocradapi.models.MessageEntity;
import com.example.daocradapi.models.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Component
public class MessageEntityMapper implements RowMapper<MessageEntity>
{
   // private JdbcTemplate jdbcTemplate;



    @Override
    public MessageEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setId(resultSet.getInt("id"));
        messageEntity.setName(resultSet.getString("name"));
        messageEntity.setTheme(resultSet.getString("theme"));
        messageEntity.setMessageDate(resultSet.getDate("message_date").toLocalDate());
        messageEntity.setMessageContent(resultSet.getString("message_content"));

        // Получаем person_id
        int personId = resultSet.getInt("person_id");

        // Получаем связанного человека
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        Person person = jdbcTemplate.queryForObject(
                "SELECT * FROM person2 WHERE id = ?",
                new Object[]{personId},
                new PersonMapper()
        );
        messageEntity.setPerson(person);
        return messageEntity;
    }

}

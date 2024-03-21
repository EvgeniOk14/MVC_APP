package com.example.daocradapi.dao.messageEntity;

import com.example.daocradapi.dao.person.PersonMapper;
import com.example.daocradapi.models.messageEntity.MessageEntity;
import com.example.daocradapi.models.person.Person;
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
    @Override
    public MessageEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException
    {
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setId(resultSet.getInt("id"));
        messageEntity.setName(resultSet.getString("name"));
        messageEntity.setTheme(resultSet.getString("theme"));
        messageEntity.setMessageDate(resultSet.getDate("message_date").toLocalDate());
        messageEntity.setMessageContent(resultSet.getString("message_content"));

        int personId = resultSet.getInt("person_id"); // Получаем person_id

        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        Person person = jdbcTemplate.queryForObject("SELECT * FROM person WHERE id = ?",
                new Object[]{personId},
                new PersonMapper());
        messageEntity.setPerson(person);  // Получаем связанного человека
        return messageEntity;
    }
}

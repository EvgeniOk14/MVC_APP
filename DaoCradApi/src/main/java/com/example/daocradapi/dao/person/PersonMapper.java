package com.example.daocradapi.dao.person;

import com.example.daocradapi.dao.messageEntity.MessageEntityMapper;
import com.example.daocradapi.models.messageEntity.MessageEntity;
import com.example.daocradapi.models.person.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
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

        // Получаем список сообщений для данного человека
        List<MessageEntity> messages = getMessagesForPerson(person.getId());
        //записываем новое сообщение в список для данного человека
        person.setMessages(messages);

        return person;
    }
    /** получить список сообщений пользователя по его id **/
    private List<MessageEntity> getMessagesForPerson(Integer personId)
    {
        String sql = "SELECT * FROM messages WHERE person_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(sql, new Object[]{personId}, new MessageEntityMapper());
    }
}


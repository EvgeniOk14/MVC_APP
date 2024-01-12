package com.example.daocradapi.dao;

import com.example.daocradapi.models.MessageEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageEntityMapper implements RowMapper<MessageEntity> {
    @Override
    public MessageEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setId(resultSet.getInt("id"));
        messageEntity.setName(resultSet.getString("name"));
       //messageEntity.setPerson_id(resultSet.getInt("person_id"));
        messageEntity.setTheme(resultSet.getString("theme"));
        messageEntity.setMessageDate(resultSet.getDate("message_date").toLocalDate());
        messageEntity.setMessageContent(resultSet.getString("message_content"));

        return messageEntity;
    }
}

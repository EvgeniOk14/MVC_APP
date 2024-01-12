package com.example.daocradapi.dao;

import com.example.daocradapi.JdbcPersonRepository;
import com.example.daocradapi.models.MessageEntity;
import com.example.daocradapi.models.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
@Component
@Transactional
public class MessageEntityDAO
{
    private final JdbcTemplate jdbcTemplate;
    private final JdbcPersonRepository jdbcPersonRepository;
    private final PersonDAO personDAO;


    public MessageEntityDAO(JdbcTemplate jdbcTemplate, JdbcPersonRepository jdbcPersonRepository, PersonDAO personDAO)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcPersonRepository = jdbcPersonRepository;
        this.personDAO = personDAO;
    }


    public void saveMessage(MessageEntity message)
    {
        String sql = "INSERT INTO messages (person_id, theme, message_date, message_content) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                message.getPerson_id(),
                message.getTheme(),
                Date.valueOf(message.getMessageDate()),
                message.getMessageContent()
        );
    }

    public List<MessageEntity> getMessagesByPersonId(Integer personId)
    {
        String sql = "SELECT * FROM messages WHERE person_id = ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{personId},
                new MessageEntityMapper()
        );
    }

    public void saveMessageEntity(MessageEntity messageEntity) {
        String sql = "INSERT INTO messages (person_id, theme, message_date, message_content) VALUES (?, ?, ?, ?)";

        LocalDate localDate = messageEntity.getMessageDate();
        Date sqlDate = Date.valueOf(localDate);

        String email = messageEntity.getEmail();
        Person person = personDAO.getPersonByEmail(email); //!!!!!!!!!!!!!!

        jdbcTemplate.update(sql, messageEntity.getPerson_id(), messageEntity.getTheme(), sqlDate, messageEntity.getMessageContent());
    }




public void saveContactForm(MessageEntity messageEntity, Integer person_id) {
    String sql = "INSERT INTO messages (person_id, theme, message_date, message_content) VALUES (?, ?, ?, ?)";

    LocalDate utilDateStr = messageEntity.getMessageDate();
    java.sql.Date sqlDate = java.sql.Date.valueOf(utilDateStr);

    // Найти id пользователя по email из формы
//    Integer person_Id = jdbcTemplate.queryForObject(
//            "SELECT id FROM person2 WHERE email = ?",
//            new Object[]{messageEntity.getEmail()},
//            Integer.class);

    jdbcTemplate.update(sql, person_id, messageEntity.getTheme(), sqlDate, messageEntity.getMessageContent());
}
}

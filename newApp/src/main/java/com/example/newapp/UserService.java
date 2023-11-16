package com.example.newapp;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserService {
    private DatabaseHandler dbHandler = new DatabaseHandler();

    public void signUpUser(User user) {
        // Здесь вы можете добавить дополнительную логику, если это необходимо
        dbHandler.signUpUser(user);
    }

    public User getUser(User user) {
        // Здесь вы можете добавить дополнительную логику, если это необходимо
        ResultSet resultSet = dbHandler.getUser(user);
        // Преобразование ResultSet в объект User и возврат
        // Вам нужно реализовать этот метод в соответствии с вашими требованиями
        return convertResultSetToUser(resultSet);
    }

    private User convertResultSetToUser(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                String firstName = resultSet.getString(Const.USERS_FIRSTNAME);
                String lastName = resultSet.getString(Const.USERS_LASTNAME);
                String userName = resultSet.getString(Const.USERS_USERNAME);
                String password = resultSet.getString(Const.USERS_PASSWORD);
                String location = resultSet.getString(Const.USERS_LOCATION);
                String gender = resultSet.getString(Const.USERS_GENDER);

                return new User(firstName, lastName, userName, password, location, gender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // В случае ошибки или отсутствия данных
    }

    // Другие методы, например, удаление пользователя и т. д.

    public void showAllUsers1()
    {
        try
        {
            textArea1.clear();
            String query = " SELECT * FROM " + Const.USERS_TABLE;
            DatabaseHandler datebaseHandler = new DatabaseHandler();
            Connection connection = datebaseHandler.getDbConnection();

            PreparedStatement prSt = datebaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = prSt.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(Const.USERS_ID);
                String firstName = resultSet.getString(Const.USERS_FIRSTNAME);
                String lastName = resultSet.getString(Const.USERS_LASTNAME);
                String userName = resultSet.getString(Const.USERS_USERNAME);
                String password =  resultSet.getString(Const.USERS_PASSWORD);
                String location = resultSet.getString(Const.USERS_LOCATION);
                String gender = resultSet.getString(Const.USERS_GENDER);

                StringBuilder sb = new StringBuilder();

                sb.append("\n").append("порядковый номер: ").append(id).append("\n")
                        .append("Имя пользователя: ").append(firstName).append("\n")
                        .append("фамилия пользователя: ").append(lastName).append("\n")
                        .append("логин пользователя: ").append(userName).append("\n")
                        .append("пароль пользователя: ").append(password).append("\n")
                        .append("Страна проживания пользователя: ").append(location).append("\n")
                        .append("Пол пользователя: ").append(gender).append("\n");
                textArea1.appendText(sb.toString());

            }
            resultSet.close();
            prSt.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}

package com.example.newapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.*;
@Component
public class DatabaseHandler
{
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;
    Connection dbConnection;


    public Connection getDbConnection() throws ClassNotFoundException, SQLException
    {
//        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        return dbConnection;
    }

    public void signUpUser(User user)
    {
        String insert = "INSERT INTO " + Const.USERS_TABLE
                + "("
                + Const.USERS_FIRSTNAME + ","
                + Const.USERS_LASTNAME + ","
                + Const.USERS_USERNAME + ","
                + Const.USERS_PASSWORD + ","
                + Const.USERS_LOCATION + ","
                + Const.USERS_GENDER
                + ")"
                + "VALUES(?,?,?,?,?,?)";


        try
        {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);

            prSt.setString(1, user.getFirstName());
            prSt.setString(2, user.getLastName());
            prSt.setString(3, user.getUerName());
            prSt.setString(4, user.getPassword());
            prSt.setString(5, user.getLocation());
            prSt.setString(6, user.getGender());
            prSt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(User user)
    {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USERS_TABLE + " WHERE "
                + Const.USERS_USERNAME + "=? AND "
                + Const.USERS_PASSWORD + "=?";

        try
        {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUerName());
            prSt.setString(2, user.getPassword());
            resSet = prSt.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return resSet;
    }


    public void deleteUser(int id)
    {
        String query = "DELETE FROM " + Const.USERS_TABLE + " WHERE "
                + Const.USERS_ID + " =?";
        try
        {
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.setInt(1, id);
            int quantity = prSt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteUserByName(String name)
    {
        String query = "DELETE FROM " + Const.USERS_TABLE + " WHERE "
                + Const.USERS_FIRSTNAME + " =?";
        try
        {
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.setString(1, name);
            int quantity = prSt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}

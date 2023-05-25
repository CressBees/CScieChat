package com.CScieChat.task;

import com.CScieChat.Main;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    public void setup() {
        try {
            /*
             * Create a connection to a local database file using SQLite
             * If the file does not already exist the DB file will already be created
             */
            Connection connection = DriverManager.getConnection("jdbc:sqlite:CScieChat.db");
            Statement statement = connection.createStatement(); // This creates a statement allowing us to execute SQL in the database
            /*
             * Database naming scheme:
             * Don't use uppercase, SQL is case-insensitive so UnderValue and Undervalue are basically the same. Instead, use underscores
             * Use plurals, SQL usually has "user" as a reserved word, which can clash with a "user" table.
             *
             * Create three sql functions to create two tables. This is mostly self-explanatory, but I will explain the SQL written here
             * CREATE TABLE IF NOT EXISTS (table name) checks to see if a table exists in a database then creates one if it does not exist
             * Everything in the brackets is the data that's stored in each row, each one being a "column"
             * INTEGER and STRING are data types, similar to the same ones used in Java already
             * PRIMARY KEY refers to the primary key of a table, this can be used as a foreign key in other tables to refer to that table.
             * "SELECT * FROM userAccounts" selects everything from the userAccounts table,
             * "WHERE username = 'SYSTEM'" filters that to only instances where the username column has "SYSTEM" as the username
             */
            String sql = "CREATE TABLE IF NOT EXISTS messages (unix_time INTEGER PRIMARY KEY, handler STRING)";
            String sql2 = "CREATE TABLE IF NOT EXISTS user_accounts (username STRING PRIMARY KEY, pass STRING)";
            String sql3 = "CREATE TABLE IF NOT EXISTS illegal_uns (name STRING PRIMARY KEY, type STRING)";
            String sql4 = "SELECT * FROM illegal_uns WHERE name = 'SYSTEM'";
            // Execute the SQL and close the file
            // (would close the connection if it was connected to MySQL or something similar, however SQLite is file based)
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql3);
            ResultSet result = statement.executeQuery(sql4);
            if(result.getString("name") == null) {
                String sql5 = "INSERT INTO illegal_uns(name, type) VALUES ('SYSTEM', 'match')";
                statement.executeUpdate(sql5);
                System.out.println("Added SYSTEM restriction");
            } else {
                String user = result.getString("name");
                System.out.println(user);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            Logger LOGGER = Logger.getLogger(DataBase.class.getName());
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }
}

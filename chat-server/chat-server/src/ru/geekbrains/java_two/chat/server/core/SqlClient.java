package ru.geekbrains.java_two.chat.server.core;

import java.sql.*;

public class SqlClient {
    private static Connection connection;
    private static Statement statement;
    private final static String QUERY_GET_NICKNAME="select nickname from clients where login = '%s' and password = '%s'",
            QUERY_GET_LOGIN_BY_LOGIN="select login from clients where login = '%s'",
            QUERY_ADD_CLIENT="insert into clients (login, password, nickname) values ('%s','%s','%s')",
            QUERY_CHANGE_NICKNAME="update clients set nickname='%s' where login='%s'" ;

    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat-server.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static String getNickname(String login, String password) {
        String query = String.format(QUERY_GET_NICKNAME,
                login, password);

        try (ResultSet set = statement.executeQuery(query)) {
            if (set.next())
                return set.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    synchronized static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static boolean registration(String login, String password, String nickName) throws SQLException{
        String query = String.format(QUERY_GET_LOGIN_BY_LOGIN, login);
        ResultSet set =statement.executeQuery(query);
            if (set.next()) return false;
        query=String.format(QUERY_ADD_CLIENT,login,password,nickName);
        statement.executeUpdate(query);
        return true;
    }

    public static boolean changeNickname(String newNickname, String login) {
        String query=String.format(QUERY_CHANGE_NICKNAME,newNickname,login);
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}

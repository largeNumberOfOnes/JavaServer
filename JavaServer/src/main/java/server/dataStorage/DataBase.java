package server.dataStorage;

import initial.Context;
import initial.MyLogger;
import server.dataStorage.exceptions.DataBaseException;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class DataBase implements Closeable {

    private static DataBase instance = null;
    private Connection connection = null;
    private Statement statement = null;

    public DataBase() throws DataBaseException {
        if (instance != null) {
            throw new DataBaseException("Error: Instance already exists");
        }

        MyLogger logger = MyLogger.getInstance();

        logger.info("Trying to connect to the database");
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL JDBC Driver successfully connected");

            connection = DriverManager.getConnection(Context.DBURL, Context.DBUser, Context.DBPass);
            statement = connection.createStatement();
            instance = this;
            logger.info("Success connection to DB");

        } catch (ClassNotFoundException e) {
            logger.sever("PostgreSQL JDBC Driver is not found", e);
        }
        catch (Exception e) {
            logger.sever("Connection to Store DB fault!", e);
        }
    }

    public static DataBase getInstance() throws DataBaseException {
        if (instance == null) {
            throw new DataBaseException("Error: no instance");
        }
        return instance;
    }

    @Override
    public void close() throws IOException {

        MyLogger logger = MyLogger.getInstance();

        try {
            connection.close();
            logger.info("Success closing the database connection");
        }
        catch (SQLException e) {
            logger.sever("Failure when closing the database connection", e);
        }
    }

    public void executeUpdate(String request) throws SQLException { statement.executeUpdate(request); }
    public void executeQuery(String request)  throws SQLException { statement.executeQuery(request);  }

    public boolean isUserExist(String login) throws DataBaseException {
        MyLogger logger = MyLogger.getInstance();
        try {
            String command = String.format("SELECT login, pass FROM users WHERE login = '%s';", login);
//            String command = String.format("EXISTS(SELECT login FROM users WHERE login = %s)", login);
            ResultSet res = statement.executeQuery(command);
            if (!res.isBeforeFirst()) {
                return false;
            }
        }
        catch (SQLException e) {
            logger.warning("Error while checking user existence", e);
            throw new DataBaseException("Error while checking user existence");
        }
        return true;
    }

    public void addUser(String login, String pass) throws DataBaseException {
        if (isUserExist(login)) {
            throw new DataBaseException("User already exists");
        }
        MyLogger logger = MyLogger.getInstance();
        try {
            String command = String.format("INSERT INTO users (login, pass, user_data) VALUES ('%s', '%s', '');", login, pass);
            statement.executeUpdate(command);
        }
        catch (SQLException e) {
            logger.warning("Error while adding user to database", e);
            throw new DataBaseException("Error while adding user to database");
        }
    }

    public void removeUser(String login, String pass) throws DataBaseException {
        if (!isUserExist(login)) {
            throw new DataBaseException("No such user");
        }
        MyLogger logger = MyLogger.getInstance();
        try {
            String command = String.format("DELETE FROM users WHERE login = '%s';", login);
            statement.executeUpdate(command);
        }
        catch (SQLException e) {
            logger.warning("Error while removing user to database", e);
            throw new DataBaseException("Error while removing user to database");
        }
    }

    public User getUser(String login) throws DataBaseException {
        MyLogger logger = MyLogger.getInstance();
        try {
            String command = "SELECT login, pass, user_data FROM users WHERE login = '%s';".formatted(login);
            ResultSet res = statement.executeQuery(command);
            var str = new StringBuilder();
            User user = null;
            res.next();
            user = new User(
                res.getString(1),
                res.getString(2),
                res.getString(3)
            );
            return user;
        }
        catch (SQLException e) {
            logger.warning("Error while getting user from database", e);
            throw new DataBaseException("Error while getting user from database");
        }
    }

    public String getAllUsers() throws DataBaseException {
        MyLogger logger = MyLogger.getInstance();
        try {
            String command = "SELECT login, pass FROM users;";
            ResultSet res = statement.executeQuery(command);
            var str = new StringBuilder();
            while (res.next()) {
                str.append(res.getString(1));
                str.append(";");
                str.append(res.getString(2));
            }
            return str.toString();
        }
        catch (SQLException e) {
            logger.warning("Error while getting all users from database", e);
            throw new DataBaseException("Error while getting all users from database");
        }
    }

    public void setUserData(String login, String data) throws DataBaseException {
        MyLogger logger = MyLogger.getInstance();
        try {
            String command = "UPDATE users SET user_data = '%s' WHERE login = '%s';".formatted(data, login);
            statement.executeUpdate(command);
        }
        catch (SQLException e) {
            logger.warning("Error while setting user data into database", e);
            throw new DataBaseException("Error while setting user data into database");
        }
    }

//    public boolean loginUser(String login, String pass) {
//        try {
//            String command = String.format("SELECT login, pass FROM public.users WHERE login = %s;", login);
//            ResultSet res = statement.executeQuery(command);
//            if (!res.isBeforeFirst()) {
//                return false;
//            }
//            res.getString("login");
//            res.getString("pass");
//            return true;
//        }
//        catch (SQLException e) {
//            return false;
//        }
//    }
//
//    public boolean registerNewUser(String login, String pass, String data) {
//        try {
//            String command = String.format("SELECT id, login, pass public.products WHERE login = %s;", login);
//            ResultSet res = statement.executeQuery(command);
//            if (!res.isBeforeFirst()) {
//                command = String.format("INSERT INTO users (login, pass, user_data) VALUES (%s, %s, %s)", login, pass, data);
//                statement.executeUpdate(command);
//                return true;
//            }
//            return false;
//        }
//        catch (SQLException e) {
//            return false;
//        }
//    }
}

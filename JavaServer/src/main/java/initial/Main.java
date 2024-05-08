package initial;

//import com.mysql.jdbc.jdbc2.optional.JDBC4StatementWrapper;
import muzzle.Muzzle;
import server.HttpRequest;
import server.Server;
import server.ServerAnswer;
import server.dataStorage.*;
import server.dataStorage.exceptions.DataCacheException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import static java.sql.DriverManager.getConnection;

//import java.sql.Statement;
import java.util.logging.Level;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        try (MyLogger logger = new MyLogger()){
            MyLogger.getInstance().info("Start main");

            Thread muzzleThread = new Thread(new Muzzle(logger));
            muzzleThread.start();
            logger.log(Level.INFO, "Starting MainFrame");
////
            Thread serverThread = new Thread(new Server());
            serverThread.start();
            logger.log(Level.INFO, "Starting Server");
////
            muzzleThread.join();
            serverThread.join();

//            String str = "w235";
//            System.out.println(str);
//            String fileStr = Base64.getEncoder().encodeToString(str.getBytes());
//            System.out.println(fileStr);
//            byte[] ba = Base64.getDecoder().decode(fileStr);
//            System.out.println(new String(ba));

//            String str = "w235";
//            System.out.println(str);
//            byte[] ba = Base64.getDecoder().decode(str);
//            System.out.println(new String(ba));
//            String fileStr = Base64.getEncoder().encodeToString(ba);
//            System.out.println(fileStr);

//            testDataCache();
//            testDataServer();
//            testGSON();

            MyLogger.getInstance().info("Close main");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // TODO: log database actions

    static void testGSON() {

        try (DataStorage dataStorage = new DataStorage()) {

//            DataStorage dataStorage = DataStorage.getInstance();

//            dataStorage.SessionFromStr();
//            HttpRequest request = new HttpRequest("""GET /hi/hi HTTP/1.1
//                    Connection: keep-alive
//                    Host: 192.168.1.137:8080
//                    Upgrade-Insecure-Requests: 1
//
//                    bodybodybodybodybodybody
//
//                    """);
//            ServerAnswer answer = new ServerAnswer("HTTP/1.1 832 Khf")
//                    .setHeader("kkkk", "value")
//                    .setHeader("tttt", "this is a final countdown")
//                    .setBodyPart("move it move it");
//
//            Session session = new Session(request, answer);
//
//            System.out.println(dataStorage.SessionToStr(session));


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void testDataCache() {

        try (DataCache dataCache = new DataCache()) {

//            dataCache.pushString("234", "----str----");
            String str = dataCache.getString("234");
            System.out.println(str);
        }
        catch (DataCacheException e) {
            e.printStackTrace();
        }

    }

    static void testDataStorage() {

        try (DataStorage dataStorage = new DataStorage()) {
//            dataStorage = DataStorage.getInstance();

//            dataStorage.login("mititka", "111");
            dataStorage.login("mititka", "stringPass");
            dataStorage.login("mititkaa", "stringPass");

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    static void testDataServer() {

        try {
            DataServer ser = new DataServer();
            ser.loadPage("/public/index.html");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Deprecated // testing data base
    static void testDataBase() {

        try (DataBase dataBase = new DataBase()) {

//            boolean fl = dataBase.isUserExist("mititka");
//            boolean fl = dataBase.isUserExist("mititkaa");
//            System.out.println("fl = " + fl);
//            String res = dataBase.getAllUsers();
//            System.out.println("res = " + res);

//            dataBase.addUser("Andrey", "saperAndrey");

//            dataBase.addUser("Petr", "Kapitsa");
//            dataBase.removeUser("Petr", "Kapitsa");

//            dataBase.setUserData("Andrey", "SELECT — оператор запроса в языке SQL, возвращающий набор данных из базы данных. Оператор возвращает ноль или более строк. Список возвращаемых столбцов задается в части оператора, называемой предложением SELECT.");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Deprecated
    public static void bu() {

        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            try(Connection connection = DriverManager.getConnection("jdbc:postgresql://192.168.56.101:5432/postgres", "vudb", "pass"); ) {
//            try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres", "postgres", "pass"); ) {
                System.out.println("Connection to Store DB successful!");
                Statement statement = connection.createStatement();
//                String command = "INSERT INTO users (id, first_name, last_name, email) VALUES (3, 'Ilya', 'Muromets', 'ilyamuromets@yandex.ru');";
                String command = "SELECT id, first_name, last_name, email FROM public.users WHERE id = 3;";
//                String command = "SELECT Id, ProductName, Price FROM public.products;";
//                String command = "CREATE TABLE products (Id INT PRIMARY KEY, ProductName VARCHAR(20), Price INT)";
//                 statement.executeUpdate(command);
                ResultSet res = statement.executeQuery(command);
                if (!res.isBeforeFirst()) {
                    System.out.println("None");
                }
                while(res.next()){

                    int id = res.getInt(1);
                    String first_name = res.getString(2);
                    String last_name = res.getString(3);
                    String email = res.getString(4);
                    System.out.printf("%d. %s %s %s \n", id, first_name, last_name, email);
                }
            }
            catch (SQLException e) {
//                System.out.println(e);
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            System.out.println("Connection to Store DB fault!");
            System.out.println(e);
        }
    }
}

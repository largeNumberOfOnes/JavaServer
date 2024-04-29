package server.dataStorage;

import java.sql.ResultSet;

public class StorageRequestProcessor {

    public static String login(String login, String pass) {
//        DataBase db = DataBase.getInstance();
//        try {
////            String command = String.format("SELECT login, pass FROM public.users WHERE login = %s;", login);
////            ResultSet res = statement.executeQuery(command);
////            if (!res.isBeforeFirst()) {
////                return false;
////            }
////            res.getString("login");
////            res.getString("pass");
////            return true;
//        }
//        catch (SQLException e) {
//            return false;
//        }
        return null;
    }

    public static boolean register() {
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
        return true;
    }

    public static void getAllUsers() {}
    public static void getAllRequests() {}

}

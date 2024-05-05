package server;

import initial.Context;
import initial.MyLogger;
import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.DataBaseException;
import server.dataStorage.exceptions.DataStorageException;
import server.dataStorage.exceptions.IncorrectPasswordException;
import server.dataStorage.exceptions.NoSuchUserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerAnswer {

    private String mes = "";
    private HashMap<String, String> headers = new HashMap<String, String>();
    private ArrayList<String> body = new ArrayList<String>();

    public ServerAnswer(String mes) {
        this.mes = mes + "\n\n";
    }

    public ServerAnswer setHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    public ServerAnswer setBodyPart(String part) {
        body.add(part);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(mes);
        for (var q : headers.entrySet()) {
            str.append(q.getKey());
            str.append(": ");
            str.append(q.getValue());
            str.append("\n");
        }
        str.append("\n");
        for (var q : body) {
            str.append(q);
            str.append("\n\n");
        }
        return mes;
    }

    public static final ServerAnswer NotFound = new ServerAnswer("HTTP/1.1 404 Not Found");
    public static final ServerAnswer Forbidden = new ServerAnswer("HTTP/1.1 403 Forbidden");
    public static final ServerAnswer BadRequest = new ServerAnswer("HTTP/1.1 400 Bad Request");
    public static final ServerAnswer InternalServerError = new ServerAnswer("HTTP/1.1 500 Internal Server Error");

// ---------------------------------------------------------------------------------------------------------------------

//    private static boolean haveAccessByHeaders(HttpRequest request, DataStorage dataStorage) {
//        if (!request.body.containsKey("login") || !request.body.containsKey("password")) {
//            return false;
//        }
//        String pass = request.body.get("password");
//        String login = request.body.get("login");
//        return dataStorage.checkLoginHash(dataStorage.createSessionIdentifier(login, pass));
//    }
//
//    private static boolean haveAccessByCookie(HttpRequest request, DataStorage dataStorage) {
//        if (request.body.containsKey("Cookie")) {
//            String[] cookies = request.body.get("Cookie").split(";");
//            for (var q : cookies) {
//                String[] arr = q.strip().split("=");
//                if (arr[0].equals("sessionIdentifier")) {
//                    if (dataStorage.checkLoginHash(arr[1])) {
//                        return true;
//                    }
//                    break;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static boolean haveAccess(HttpRequest request) {
//        try {
//            DataStorage dataStorage = DataStorage.getInstance();
//            return haveAccessByCookie(request, dataStorage) || haveAccessByHeaders(request, dataStorage);
//        }
//        catch (DataStorageException e) {
//            return false;
//        }
//    }

}

package server.requestProcessor;

import initial.Context;
import initial.MyLogger;
import server.HttpRequest;
import server.ServerAnswer;
import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.DataStorageException;
import server.dataStorage.exceptions.IncorrectPasswordException;
import server.dataStorage.exceptions.NoSuchUserException;

public class ProcessPOST {

    public static ServerAnswer process(HttpRequest request) {
        if (request.getPath().equals("/users/register")) {
    //            StorageRequestProcessor.login();
            return ServerAnswer.NotFound;
        }
        else if (request.getPath().equals("/users/login")) {
//            return loginRequest(request);
            return null;
        } else {
            return ServerAnswer.NotFound;
        }
    }

//    private static String loginRequest(HttpRequest request) {
//        MyLogger logger = MyLogger.getInstance();
//        try {
//            DataStorage dataStorage = DataStorage.getInstance();
//            if (!request.body.containsKey("login") || !request.body.containsKey("password")) {
//                logger.warning("the request does not contain a password or login");
//                return BadRequest.mes;
//            }
//            String login = request.body.get("login");
//            String pass  = request.body.get("password");
//            dataStorage.login(login, pass);
//            dataStorage.putLoginHash(login, pass);
//            StringBuilder str = new StringBuilder();
//
//            str.append("HTTP/1.1 200 OK\n");
//            str.append("Redirect: %s\n".formatted(Context.afterLoginPage));
//            str.append("Set-Cookie: sessionIdentifier=%s\n".formatted(dataStorage.createLoginHash(login, pass)));
//            str.append("\n");
//            str.append("iehfblsviewhalgvbi\n");
//            str.append("\n");
//            return str.toString();
//        }
//        catch (IncorrectPasswordException e) {
//            logger.warning("Error connecting to data storage", e);
//            return ServerAnswer.Forbidden;
//        }
//        catch (NoSuchUserException e) {
//            logger.warning("Error: no such user [%s]".formatted(""), e);
//            return ServerAnswer.Forbidden;
//        }
//        catch (DataStorageException e) {
//            logger.warning("Error connecting to data storage", e);
//            return ServerAnswer.InternalServerError;
//        }
//    }
}

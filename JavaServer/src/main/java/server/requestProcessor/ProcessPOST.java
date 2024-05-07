package server.requestProcessor;

import initial.Context;
import initial.MyLogger;
import server.HttpRequest;
import server.ServerAnswer;
import server.SessionIdentifier;
import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.*;

public class ProcessPOST {

    public static ServerAnswer process(HttpRequest request) {
        if (request.getPath().equals("/public/users/register")) {
            return registerRequest(request);
        }
        else if (request.getPath().equals("/public/users/login")) {
            return loginRequest(request);
        }
        else if (request.getPath().equals("/public/users/logout")) {
            return logoutRequest(request);
        } else {
            return ServerAnswer.NotFound;
        }
    }

    private static ServerAnswer loginRequest(HttpRequest request) {
        MyLogger logger = MyLogger.getInstance();
        try {
            DataStorage dataStorage = DataStorage.getInstance();
            if (!request.containsHeader("login") || !request.containsHeader("password")) {
                logger.warning("the request does not contain a password or login");
                return ServerAnswer.BadRequest;
            }
//            if (request.containsHeader("Cookie")) {
//                return ServerAnswer.BadRequest;
//            }
            String login = request.getHeader("login");
            String pass  = request.getHeader("password");
            dataStorage.login(login, pass);
//            dataStorage.putLoginHash(login, pass);
//            StringBuilder str = new StringBuilder();

//            str.append("HTTP/1.1 200 OK\n");
//            str.append("Redirect: %s\n".formatted(Context.afterLoginPage));
//            str.append("Set-Cookie: sessionIdentifier=%s\n".formatted(dataStorage.createLoginHash(login, pass)));
//            str.append("\n");
//            str.append("iehfblsviewhalgvbi\n");
//            str.append("\n");

            ServerAnswer ans = new ServerAnswer("HTTP/1.1 200 OK");
            ans = ans
                .setHeader("Redirect", Context.afterLoginPage)
                .setHeader("SetSessionIdentifier", "sessionIdentifier=%s".formatted(SessionIdentifier.createAsString(login, pass)))
                ;
            return ans;
        }
        catch (IncorrectPasswordException e) {
            logger.warning("Error connecting to data storage", e);
            return ServerAnswer.Forbidden;
        }
        catch (NoSuchUserException e) {
            logger.warning("Error: no such user [%s]".formatted(""), e);
            return ServerAnswer.Forbidden;
        }
        catch (DataStorageException e) {
            logger.warning("Error connecting to data storage", e);
            return ServerAnswer.InternalServerError;
        }
    }

    private static ServerAnswer registerRequest(HttpRequest request) {
        MyLogger logger = MyLogger.getInstance();
        try {
            DataStorage dataStorage = DataStorage.getInstance();
            if (!request.containsHeader("login") || !request.containsHeader("password")) {
                logger.warning("the request does not contain a password or login");
                return ServerAnswer.BadRequest;
            }
            String login = request.getHeader("login");
            String pass  = request.getHeader("password");
            dataStorage.register(login, pass);
            dataStorage.login(login, pass);

            ServerAnswer ans = new ServerAnswer("HTTP/1.1 200 OK");
            ans = ans
//                .setHeader("Redirect", Context.afterLoginPage)
                .setHeader("SetSessionIdentifier", "sessionIdentifier=%s".formatted(SessionIdentifier.createAsString(login, pass)))
            ;
            return ans;
        }
        catch (IncorrectPasswordException e) {
            logger.warning("Error connecting to data storage", e);
            return ServerAnswer.Forbidden;
        }
        catch (UserAlreadyExistsException e) {
            logger.warning("Error: no such user [%s]".formatted(""), e);
            return ServerAnswer.Forbidden;
        }
        catch (DataStorageException e) {
            logger.warning("Error connecting to data storage", e);
            return ServerAnswer.InternalServerError;
        }
    }

    public static ServerAnswer logoutRequest(HttpRequest request) {
        MyLogger logger = MyLogger.getInstance();
        try {
            DataStorage dataStorage = DataStorage.getInstance();
            if (!request.containsHeader("Cookie")) {
                logger.warning("the request does not contain a session identifier, so just log out");
                return ServerAnswer.OK;
            }
            String cookies = request.getHeader("Cookie");

            dataStorage.logout(request.getCookie_SessionIdentifier());

            return ServerAnswer.OK;
        }
        catch (NoSuchSessionIdentifier e) {
            logger.warning("Warning no such session identifier", e);
            return ServerAnswer.OK;
        }
        catch (DataStorageException e) {
            logger.warning("Error connecting to data storage", e);
            return ServerAnswer.InternalServerError;
        }
    }
}

package server.dataStorage;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import initial.Context;
import initial.MyLogger;
import server.HttpRequest;
import server.Message;
import server.ServerAnswer;
import server.SessionIdentifier;
import server.dataStorage.exceptions.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

public class DataStorage implements Closeable {

    public static DataStorage instance = null;

    private DataBase dataBase = null;
    private DataCache dataCache = null;
    private DataServer dataServer = null;
//    private ArrayList<SessionIdentifier> sessionIdentifierArr = new ArrayList<SessionIdentifier>();

    // Initialization and access ---------------------------------------------------------------------------------------

    public DataStorage() throws DataStorageException {
        if (instance != null) {
            throw new DataStorageException("Error: Instance already exists");
        }

        MyLogger logger = MyLogger.getInstance();

        try {
            dataBase = new DataBase();
            dataCache = new DataCache();
            dataServer = new DataServer();
        }
        catch (DataBaseException e) {
            logger.sever("Connect to DB fault", e);
        }
        catch (DataCacheException e) {
            logger.sever("Connect to DC fault", e);
        }
        catch (DataServerException e) {
            logger.sever("Connect to DS fault", e);
        }

        instance = this;
        logger.info("Success connection to data storage");
    }

    @Override
    public void close() throws IOException {
        dataBase.close();
        dataCache.close();
        dataServer.close();
    }

    public static DataStorage getInstance() throws DataStorageException {
        if (instance == null) {
            throw new DataStorageException("Error: no instance");
        }
        return instance;
    }

    // Interface -------------------------------------------------------------------------------------------------------

    public void login(String login, String pass) throws DataStorageException {
        try {
            if (!dataBase.isUserExist(login)) {
                throw new NoSuchUserException(login);
            }
            User user = dataBase.getUser(login);
            if (!user.pass.equals(pass)) {
                throw new IncorrectPasswordException(pass);
            }
            putSessionIdentifier(login, pass);
        }
        catch (DataBaseException e) {
            throw new DataStorageException("Error while login user [%s]".formatted(login));
        }
    }

    public void putSessionIdentifier(String login, String pass) {
        SessionIdentifier loginHash = new SessionIdentifier(login, pass);
//        if (!sessionIdentifierArr.contains(loginHash)) {
//            sessionIdentifierArr.add(loginHash);
//        }

        String sessionIdentifier = SessionIdentifier.createAsString(login, pass);
        if (!dataCache.contains(sessionIdentifier)) {
            dataCache.putSessionIdentifier(sessionIdentifier);
        }
    }
    public void removeSessionIdentifier(SessionIdentifier id) {
//        sessionIdentifierArr.remove(id);
        dataCache.removeSessionIdentifier(id.toString());
    }

    public boolean checkSessionIdentifier(SessionIdentifier id) {
//        return !id.isNull() && sessionIdentifierArr.contains(id.toString());
//        for (var q : sessionIdentifierArr) {
//            System.out.print("--->");
//            System.out.println(q);
//        }
//        return sessionIdentifierArr.contains(id);
        return dataCache.contains(id.toString());
    }

    public void register(String login, String pass) throws DataStorageException {
        try {
            if (dataBase.isUserExist(login)) {
                throw new UserAlreadyExistsException(login);
            }
            dataBase.addUser(login, pass);
        }
        catch (DataBaseException e) {
            throw new DataStorageException("Error while register user [%s]".formatted(login));
        }
    }

    public void logout(SessionIdentifier id) throws DataStorageException {
        removeSessionIdentifier(id);
    }

    public void putStringToChat(String login, String message, String path, SessionIdentifier id) throws DataStorageException {
        try {
            if (haveAccess_toDataBase(path, id)) {
                dataBase.putStringToChat(login, message);
            } else {
                throw new AccessDenied(path);
            }
        }
        catch (DataBaseException e) {
            throw new DataStorageException("Error while putting message to database [%s]".formatted(login));
        }
    }

    public void setUserData(String login, String pass, String data) throws DataStorageException {

    }

    public void getUserData(String login, String pass) throws DataStorageException {

    }


    public Session SessionFromStr(String str) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(str, Session.class);
    }
    public String SessionToStr(Session session) {
        Gson gson = new Gson();
        return gson.toJson(session);
    }
    public void writeSessionToCache(Session session) throws DataStorageException {
        dataCache.putSessionToCache(SessionToStr(session));
    }

    public ArrayList<HttpRequest> getSessionsFromCache() throws DataStorageException {
        return null;
    }

    public ArrayList<Message> getAllChatMessages(SessionIdentifier id) throws DataStorageException {
        MyLogger logger = MyLogger.getInstance();
        try {
            if (haveAccess_toDataBase("", id)) {
                return dataBase.getAllChatMessages();
            }
            throw new AccessDenied("");
        }
        catch (DataBaseException e) {
            logger.warning("Error while loading all chat messages", e);
            throw new DataStorageException("Error while loading all chat messages");
        }
    }

    public byte[] getServerFile(String path, SessionIdentifier id) throws DataStorageException {
//        putSessionIdentifier("Oleg", "super");
        MyLogger logger = MyLogger.getInstance();
//        System.out.println("path: " + path);
        try {
            if (haveAccess_toDataServer(path, id)) {
//                System.out.println("access gained");
                byte[] b = dataServer.loadPage(path);
//                System.out.println("file loaded");
                return b;
            } else {
                throw new AccessDenied(path);
            }
        }
        catch (IOException e) {
            logger.warning("Error while loading page", e);
            throw new DataStorageException("Error while loading page");
        }
    }

    public String getFileContextType(String path) throws DataStorageException {
        try {
            return DataServer.getPageContextType(path);
        }
        catch (IOException e) {
            throw new DataStorageException(e.getMessage());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private boolean haveAccess_toDataServer(String path, SessionIdentifier id) {
        return Context.isPublic(path) || checkSessionIdentifier(id);
    }

    private boolean haveAccess_toDataBase(String path, SessionIdentifier id) {
        return Context.isPublic(path) || checkSessionIdentifier(id);
    }

}

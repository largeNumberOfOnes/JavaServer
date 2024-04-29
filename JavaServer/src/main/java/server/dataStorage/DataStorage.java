package server.dataStorage;


import initial.MyLogger;
import server.HttpRequest;
import server.dataStorage.exceptions.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

public class DataStorage implements Closeable {

    public static DataStorage instance = null;

    private DataBase dataBase = null;
    private DataCache dataCache = null;
    private ArrayList<String> loginHashArr = new ArrayList<String>();

    public DataStorage() throws DataStorageException {
        if (instance != null) {
            throw new DataStorageException("Error: Instance already exists");
        }

        MyLogger logger = MyLogger.getInstance();

        try {
            dataBase = new DataBase();
            dataCache = new DataCache();
        }
        catch (DataBaseException e) {
            logger.sever("Connect to DB fault", e);
        }
        catch (DataCacheException e) {
            logger.sever("Connect to DC fault", e);
        }

        instance = this;
        logger.info("Success connection to data storage");
    }

    public static DataStorage getInstance() throws DataStorageException {
        if (instance == null) {
            throw new DataStorageException("Error: no instance");
        }
        return instance;
    }

    public void login(String login, String pass) throws DataStorageException {
        try {
            if (!dataBase.isUserExist(login)) {
                throw new NoSuchUserException(login);
            }
            User user = dataBase.getUser(login);
            if (!user.pass.equals(pass)) {
                throw new IncorrectPasswordException(pass);
            }
            putLoginHash(login, pass);
        }
        catch (DataBaseException e) {
            throw new DataStorageException("Error while login user [%s]".formatted(login));
        }
    }

    public String createLoginHash(String login, String pass) {
        return login + pass;
    }

    public void putLoginHash(String login, String pass) {
        String loginHash = createLoginHash(login, pass);
        if (!loginHashArr.contains(loginHash)) {
            loginHashArr.add(loginHash);
        }
    }

    public boolean checkLoginHash(String hash) {
        return loginHashArr.contains(hash);
    }

    public void register(String name, String pass) throws DataStorageException {

    }

    public void setData(String name, String pass, String data) throws DataStorageException {

    }

    public void getData(String name, String pass, String data) throws DataStorageException {

    }

    public void putRequestToCache(HttpRequest request) throws DataStorageException {

    }

    public ArrayList<HttpRequest> getRequestsFromCache() throws DataStorageException {
        return null;
    }

    @Override
    public void close() throws IOException {
        dataBase.close();
        dataCache.close();
    }

}

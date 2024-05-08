package server.dataStorage;

import initial.MyLogger;
import server.SessionIdentifier;
import server.dataStorage.exceptions.DataBaseException;
import server.dataStorage.exceptions.DataCacheException;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.Closeable;
import java.text.SimpleDateFormat;
import java.util.Date;

import redis.clients.jedis.Jedis;

public class DataCache implements Closeable {

    private static DataCache instance = null;
    private Jedis jedis = null;

    private String sessionIdentifierArr = "SAR";
    private String sessionArr = "SSR";

    public DataCache() throws DataCacheException {
        if (instance != null) {
            throw new DataCacheException("Error: Instance already exists");
        }

        MyLogger logger = MyLogger.getInstance();
        logger.info("Try to connect to the dataCache");

        jedis = new Jedis();
        instance = this;

    }

    public static DataCache getInstance() throws DataCacheException {
        if (instance == null) {
            throw new DataCacheException("Error: no instance");
        }
        return instance;
    }

    public void pushString(String key, String str) {
        jedis.set(key, str);
    }

    public String getString(String key) {
        return jedis.get(key);
    }

    public boolean contains(String id) {
        return jedis.sismember(sessionIdentifierArr, id);
    }

    public void putSessionIdentifier(String id) {
        jedis.sadd(sessionIdentifierArr, id);
    }

    public void removeSessionIdentifier(String id) {
        jedis.srem(sessionIdentifierArr, id);
    }

    public void putSessionToCache(String sessionStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String date = formatter.format( new Date() );
        jedis.hset(sessionArr, date, sessionStr);
    }

    @Override
    public void close() {
        jedis.close();
    }

}

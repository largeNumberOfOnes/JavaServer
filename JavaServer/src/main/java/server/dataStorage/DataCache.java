package server.dataStorage;

import initial.MyLogger;
import server.dataStorage.exceptions.DataBaseException;
import server.dataStorage.exceptions.DataCacheException;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.Closeable;

import redis.clients.jedis.Jedis;

public class DataCache implements Closeable {

    private static DataCache instance = null;
    private Jedis jedis = null;

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


    @Override
    public void close() {
        jedis.close();
    }

}

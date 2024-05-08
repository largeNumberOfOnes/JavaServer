package server.dataStorage;

import initial.Context;
import initial.MyLogger;
import server.SessionIdentifier;
import server.dataStorage.exceptions.DataBaseException;
import server.dataStorage.exceptions.DataServerException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataServer implements Closeable {

    private static DataServer instance = null;

    // Initialization and access ---------------------------------------------------------------------------------------

    public DataServer() throws DataServerException {
        if (instance != null) {
            throw new DataServerException("Error: Instance already exists");
        }
        instance = this;

        MyLogger logger = MyLogger.getInstance();
        logger.info("Success connection to DataServer");
    }

    public static DataServer getInstance() throws DataServerException {
        if (instance == null) {
            throw new DataServerException("Error: no instance");
        }
        return instance;
    }

    @Override
    public void close() {}

    // Interface -------------------------------------------------------------------------------------------------------

    public byte[] loadPage(String path) throws IOException {
        String fullpath = Context.basePath + path;
//        System.out.println(getExtension(fullpath));
        File file = new File(fullpath);
//        System.out.println("----");
//        System.out.println("Path " + fullpath);
//        byte[] b = Files.readAllBytes(file.toPath());
//        byte[] b = Files.readAllBytes(Paths.get(fullpath));
//        System.out.println("All bytes read");
        return Files.readAllBytes(Paths.get(fullpath));
    };

    private static String getExtension(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
    }

    public static String getPageContextType(String path) throws IOException {
        String ext = getExtension(path);
        return switch (ext) {
            case "html" -> "text/html; charset=utf-8";
            case "css" -> "text/css; charset=utf-8";
            case "js" -> "text/html; charset=utf-8";
            case "ico" -> "image/x-icon";
            default -> throw new IOException("Unrecognized file extension [%s]".formatted(ext));
        };
    }

}

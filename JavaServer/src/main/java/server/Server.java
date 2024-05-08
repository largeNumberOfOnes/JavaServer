package server;

import initial.Context;
import initial.MyLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.DataStorageException;


public class Server implements Runnable {

    public static boolean woark = true;

    public void run() {

        MyLogger logger = MyLogger.getInstance();
        try {
            new Thread(new ConsoleCommandHandler()).start();
        }
        catch (Exception e) {
            logger.sever("Error while creating ConsoleCommandHandler", e);
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(Context.port);
             DataStorage storage = new DataStorage()
        ) {
            logger.info("Start HTTP request handler");

            while (!serverSocket.isClosed()) {
                if (woark) {
                    Socket socket = serverSocket.accept();
                    logger.info("Catch HTTP request");
                    new Thread(new ProcessClient(socket)).start();
                }
            }

            logger.info("Close HTTP request handler");
        }
        catch (IOException e) {
            logger.sever("Error while creating a server socket", e);
        }
        catch (DataStorageException e) {
            logger.sever("Error while connecting to database", e);
        }

    }
}

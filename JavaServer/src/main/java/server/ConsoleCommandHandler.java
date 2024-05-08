package server;

import initial.MyLogger;

import java.security.KeyPair;
import java.util.concurrent.Exchanger;

public class ConsoleCommandHandler implements Runnable {

    private static ConsoleCommandHandler instance;
    private static Server serverThread;

    private static final String str_undefCommand = "Undefined command";
    private static final String str_serverStopped = "Server stoped";
    private static final String str_serverStarted = "Server started";
    private static final String str_error = "Error";

    public ConsoleCommandHandler() throws Exception {
        if (instance != null) {
            throw new Exception("Instance already exists");
        }
        instance = this;
    }

    public static ConsoleCommandHandler getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("No instance");
        }
        return instance;
    }

    @Override
    public void run() {

        var exchanger = new Exchanger<ConsoleCommand>();
        ConsoleCommand mes = null;

        try {
            mes = exchanger.exchange(null);
            exchanger.exchange(mes);

        }
        catch (InterruptedException e) {

        }

    }

    private String getCommand(String com) {
        int i = com.indexOf(" ");
        if (i == -1) {
            return com;
        }
        return com.substring(0, i);
    }

    private String getArgs(String com) {
        int i = com.indexOf(" ");
        if (i == -1) {
            return "";
        }
        return com.substring(i+1);
    }

    public String commandHandler(String command) throws Exception {
        MyLogger.getInstance().info("Processing: %s".formatted(command));
        if (instance == null) {
            throw new Exception("No instance");
        }
//        String[] arr = command.split(" ");
        String args = getArgs(command);
        String com  = getCommand(command);
        System.out.println("args: " + args);
        System.out.println("com: " + com);
        return switch (com) {
            case "server" -> server(args);
            default -> str_undefCommand + " [%s]".formatted(com);
        };
    }

    public String server(String command) {
        MyLogger.getInstance().info("Processing: %s".formatted(command));
        String args = getCommand(command);
        String com  = getArgs(command);
        return switch (command) {
            case "stop" -> server_stop();
            case "start" -> server_start();
            default -> str_undefCommand;
        };
    }

    public String server_stop() {
        try {
            Server.woark = false;
            return str_serverStopped;
        }
        catch (Exception e) {
            e.printStackTrace();
            return str_error;
        }
    }

    public String server_start() {
        try {
            Server.woark = true;
            return str_serverStarted;
        }
        catch (Exception e) {
            e.printStackTrace();
            return str_error;
        }
    }

}

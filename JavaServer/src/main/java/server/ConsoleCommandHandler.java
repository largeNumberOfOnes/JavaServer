package server;

import initial.MyLogger;
import server.dataStorage.DataBase;
import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.DataBaseException;

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
            case "data" -> data(args);
            case "help" -> help();
            default -> str_undefCommand + " [%s]".formatted(com);
        };
    }

    public String server(String command) {
        String args = getCommand(command);
        String com  = getArgs(command);
        return switch (com) {
            case "stop" -> server_stop();
            case "start" -> server_start();
            case "status" -> server_status();
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

    public String server_status() {
        return """
        Имя: Капитан Старинных Времен
        Внешность: Старательно проработанный, собранный из всестороннего барахла, с широкими плечами, глубоким голосом и внушительной бородой, длиной до public static void main. Он всегда одет в старинный морской костюм, украшенный яркими эполетами и массивными золотыми кнопками. На его голове всегда старинная треугольная шляпа, украшенная пером и StackTrace-ом.
        Характер: Скрытный. Не женат.
        Мотивация: to PostgreSQL JDBC Driver successfully.
        Особенности: Капитан Старинных Времен обладает внушительным опытом в чтении логов. Он знает каждый уголок океана и умеет управлять своим кораблем даже в самых сложных условиях. Кроме того, он владеет множеством навыков, необходимых для выживания на необитаемых островах и в битвах с другими пиратами.
        Девиз: /com/formdev/flatlaf
        """;
    }

    public String data(String command) {
        String com  = getCommand(command);
        String args = getArgs(command);
        return switch (com) {
            case "isUserExists" -> data_isUserExists(args);
            default -> str_undefCommand;
        };
    }

    public String data_isUserExists(String command) {
        String args = getCommand(command);
        try {
            if (DataBase.getInstance().isUserExist(args)) {
                return "true";
            } else {
                return "false";
            }
        }
        catch (DataBaseException e) {
            return str_error;
        }
    }

    public String help() {
        return """
               server stop
               server start
               server status
               data isUserExists
               help
               """;
    }

}

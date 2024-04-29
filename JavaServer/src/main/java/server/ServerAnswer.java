package server;

import initial.Context;
import initial.MyLogger;
import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.DataBaseException;
import server.dataStorage.exceptions.DataStorageException;
import server.dataStorage.exceptions.IncorrectPasswordException;
import server.dataStorage.exceptions.NoSuchUserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class ServerAnswer {
    private String mes = "";
    ServerAnswer(String mes) {
        this.mes = mes + "\n\n";
    }
    ServerAnswer(HttpRequest request) {
        String ans = processRequest(request);
        this.mes = ans;
    }

    public String toString() {
        return mes;
    }

    static final ServerAnswer NotFound = new ServerAnswer("HTTP/1.1 404 Not Found");
    static final ServerAnswer Forbidden = new ServerAnswer("HTTP/1.1 403 Forbidden");
    static final ServerAnswer BadRequest = new ServerAnswer("HTTP/1.1 400 Bad Request");
    static final ServerAnswer InternalServerError = new ServerAnswer("HTTP/1.1 500 Internal Server Error");


// ---------------------------------------------------------------------------------------------------------------------

    private static boolean haveAccessByHeaders(HttpRequest request, DataStorage dataStorage) {
        if (!request.body.containsKey("login") || !request.body.containsKey("password")) {
            return false;
        }
        String pass = request.body.get("password");
        String login = request.body.get("login");
        return dataStorage.checkLoginHash(dataStorage.createLoginHash(login, pass));
    }

    private static boolean haveAccessByCookie(HttpRequest request, DataStorage dataStorage) {
        if (request.body.containsKey("Cookie")) {
            String[] cookies = request.body.get("Cookie").split(";");
            for (var q : cookies) {
                String[] arr = q.strip().split("=");
                if (arr[0].equals("loginHash")) {
                    if (dataStorage.checkLoginHash(arr[1])) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }


    public static boolean haveAccess(HttpRequest request) {
        try {
            DataStorage dataStorage = DataStorage.getInstance();
            return haveAccessByCookie(request, dataStorage) || haveAccessByHeaders(request, dataStorage);
        }
        catch (DataStorageException e) {
            return false;
        }
    }

    public static String processRequest(HttpRequest request) {
        if (request.isGET()) {
            return processGET(request);
        } else if (request.isPOST()) {
            return processPOST(request);
        } else {
            return NotFound.mes;
        }
    }

    private static String processGET(HttpRequest request) {
        if (request.getPath().equals("/")) {
//            return return_Page("/public/index.html");
            return return_Page(Context.mainPage);
        } else if (Context.isPublic(request.getPath())) {
            return return_Page(request.getPath());
        } else if (haveAccess(request)) {
            return return_Page(request.getPath());
        } else {
            return Forbidden.mes;
        }
    }

    private static String processPOST(HttpRequest request) {
        if (request.getPath().equals("/users/register")) {
//            StorageRequestProcessor.login();
            return NotFound.mes;
        }
        else if (request.getPath().equals("/users/login")) {
            return loginRequest(request);
        } else {
            return NotFound.mes;
        }
    }

    private static String loginRequest(HttpRequest request) {
        MyLogger logger = MyLogger.getInstance();
        try {
            DataStorage dataStorage = DataStorage.getInstance();
            if (!request.body.containsKey("login") || !request.body.containsKey("password")) {
                logger.warning("the request does not contain a password or login");
                return BadRequest.mes;
            }
            String login = request.body.get("login");
            String pass  = request.body.get("password");
            dataStorage.login(login, pass);
            dataStorage.putLoginHash(login, pass);
            StringBuilder str = new StringBuilder();
//            str.append("HTTP/1.1 308 Permanent Redirect\n");
//            str.append("HTTP/1.1 303 See Other\n");
//            str.append("Location: %s\n".formatted(Context.afterLoginPage));
//            str.append("Set-Cookie: loginHash=%s\n".formatted(dataStorage.createLoginHash(login, pass)));
//            str.append("\n");

            str.append("HTTP/1.1 200 OK\n");
            str.append("Redirect: %s\n".formatted(Context.afterLoginPage));
            str.append("Set-Cookie: loginHash=%s\n".formatted(dataStorage.createLoginHash(login, pass)));
            str.append("\n");
            str.append("iehfblsviewhalgvbi\n");
            str.append("\n");
            return str.toString();
        }
        catch (IncorrectPasswordException e) {
            logger.warning("Error connecting to data storage", e);
            return Forbidden.mes;
        }
        catch (NoSuchUserException e) {
            logger.warning("Error: no such user [%s]".formatted(""), e);
            return Forbidden.mes;
        }
        catch (DataStorageException e) {
            logger.warning("Error connecting to data storage", e);
            return InternalServerError.mes;
        }
    }

    private static String return_Page(String respath) {
        try {
//            System.out.println(Context.basePath + request.getPath());
            String path = Context.basePath + respath;
            byte[] file = loadPage(path);
//            String fileStr = Base64.getEncoder().encodeToString(file);
            String fileStr = new String(file);
            String ans = "";
//            System.out.println(new String(file));
            ans += "HTTP/1.1 200 OK\n";
            ans += "Content-Type: %s\n".formatted(getContextType(path));
            ans += "Content-Length: %d\n".formatted(file.length);
            ans += "\n";
//            ans += fileStr;
//            for(int i = 0; i < file.length; i++) {
//                ans = ans + (char)file[i];
//            }
            ans += fileStr;
            ans += "\n";
//            ans += "\n";
//            System.out.println("|" + fileStr + "|");
            return ans;
        }
        catch (FileNotFoundException e) {
            return NotFound.mes;
        }
        catch (IOException e) {
            return InternalServerError.mes;
        }
    }

    private static byte[] loadPage(String path) throws IOException {
        System.out.println(getExtension(path));
        File file = new File(path);
        return Files.readAllBytes(file.toPath());
    };

    private static String getExtension(String path) {
        return path.substring(path.lastIndexOf('.')+1);
    }

    private static String getContextType(String path) throws IOException {
        String ext = getExtension(path);
        return switch (ext) {
            case "html" -> "text/html; charset=utf-8";
            case "js" -> "text/html; charset=utf-8";
            case "ico" -> "image/x-icon";
//            case "ico" -> "image/apng";
            default -> throw new IOException("Unrecognized file extension [%s]".formatted(ext));
        };
    }

}

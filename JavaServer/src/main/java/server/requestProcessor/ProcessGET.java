package server.requestProcessor;

import initial.Context;
import initial.MyLogger;
import server.HttpRequest;
import server.ServerAnswer;
import server.SessionIdentifier;
import server.dataStorage.DataStorage;
import server.dataStorage.exceptions.AccessDenied;
import server.dataStorage.exceptions.DataStorageException;
import server.dataStorage.exceptions.ResourceNotFound;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class ProcessGET {

    public static ServerAnswer process(HttpRequest request) {
        if (request.getPath().equals("/")) {
            return return_Page(Context.mainPage, SessionIdentifier.NOSID);
        }
        else if (request.getPath().equals("/internal/users/getmes")) {
            return getMesRequest(request);
        }
        try {
            return return_Page(request.getPath(), request.getCookie_SessionIdentifier());
        }
        catch (Exception e) {
            return ServerAnswer.BadRequest;
        }
    }


    private static ServerAnswer return_Page(String path, SessionIdentifier id) {
        MyLogger logger = MyLogger.getInstance();
        logger.info("Process return_Page");
        try {

            DataStorage dataStorage = DataStorage.getInstance();
            byte[] file = dataStorage.getServerFile(path, id);
            String fileStr = new String(file);
//            byte[] file = ServerResources.loadPage(path);
//            String fileStr = Base64.getEncoder().encodeToString(file);

            ServerAnswer ans = new ServerAnswer("HTTP/1.1 200 OK");
            ans = ans
                    .setHeader("Content-Type", dataStorage.getFileContextType(path))
                    .setHeader("Content-Length", "%d".formatted(file.length))
                    .setBodyPart(fileStr)
                    ;
            return ans;
        }
        catch (AccessDenied e) {
            logger.warning("AccessDenied with [%s]".formatted(id.toString()), e);
            return ServerAnswer.Forbidden;
        }
        catch (ResourceNotFound e) {
            logger.warning("ResourceNotFound", e);
            return ServerAnswer.NotFound;
        }
        catch (DataStorageException e) {
            logger.warning("DataStorageException", e);
            return ServerAnswer.InternalServerError;
        }
    }

    private static ServerAnswer getMesRequest(HttpRequest request) {
        MyLogger logger = MyLogger.getInstance();
        try {
            DataStorage dataStorage = DataStorage.getInstance();
            SessionIdentifier id = request.getCookie_SessionIdentifier();

            var mesList = dataStorage.getAllChatMessages(id);
            Collections.reverse(mesList);

            StringBuilder str = new StringBuilder();
            for (var q : mesList) {
                str.append("<div>");
                str.append("<p class=\"sender\">%s %s: %s:</p>".formatted(q.date, q.time, q.login));
                str.append("<p>%s</p>".formatted(q.mes));
                str.append("<hr align=\"right\" width=\"3000\" size=\"4\" color=\"#ff9900\" />");
                str.append("</div>\n");
            }
//            String cookies = request.getHeader("Cookie");

            return ServerAnswer.OK.setBodyPart(str.toString());
        }
        catch (AccessDenied e) {
            logger.warning("Warning no such session identifier", e);
            return ServerAnswer.Forbidden;
        }
        catch (DataStorageException e) {
            logger.warning("Error connecting to data storage", e);
            return ServerAnswer.InternalServerError;
        }
    }

}

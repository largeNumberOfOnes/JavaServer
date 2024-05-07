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

public class ProcessGET {

    public static ServerAnswer process(HttpRequest request) {
        if (request.getPath().equals("/")) {
            return return_Page(Context.mainPage, SessionIdentifier.NOSID);
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

}

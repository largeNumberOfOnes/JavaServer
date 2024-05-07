package server.requestProcessor;

import initial.MyLogger;
import server.HttpRequest;
import server.ServerAnswer;

public class RequestProcessor {

    public static ServerAnswer process(HttpRequest request) {
        MyLogger logger = MyLogger.getInstance();
        if (request.isGET()) {
            logger.info("Start process GET");
            return ProcessGET.process(request);
        } else if (request.isPOST()) {
            logger.info("Start process POST");
            return ProcessPOST.process(request);
        } else {
            logger.info("Undefined method, return NotFound");
            return ServerAnswer.NotFound;
        }
    }

}

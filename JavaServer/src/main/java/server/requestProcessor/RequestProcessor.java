package server.requestProcessor;

import server.HttpRequest;
import server.ServerAnswer;

public class RequestProcessor {

    public static ServerAnswer process(HttpRequest request) {
        if (request.isGET()) {
            return ProcessGET.process(request);
        } else if (request.isPOST()) {
            return ProcessPOST.process(request);
        } else {
            return ServerAnswer.NotFound;
        }
    }

}

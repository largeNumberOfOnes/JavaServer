package server.dataStorage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.HttpRequest;
import server.ServerAnswer;

public class Session {

    private final String requestStarting;
    private final String answerStarting;
    private final String requestString;
    private final String answerString;

    public Session(HttpRequest request, ServerAnswer answer) {
        requestStarting = request.getMethod() + " " + request.getPath() + " " + request.getProtocol();
        answerStarting = answer.getMes();
        requestString = request.toString();
        answerString = answer.toString();
    }

}

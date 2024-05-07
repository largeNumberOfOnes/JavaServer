package server;

//import java.util.ArrayList;
import initial.Context;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private String protocol;
    private HashMap<String, String> body = new HashMap<String, String>();

    private void parseAndFillStartingLine(String str) {
        String[] arr = str.split(" ");
        method = arr[0];
        path = arr[1];
        protocol = arr[2];
    }

    @Override
    public String toString() {
        String str = "method: " + method + "\n";
        str += "path: " + path + "\n";
        str += "protocol: " + protocol + "\n";
        for (Map.Entry<String, String> entry : body.entrySet()) {
            str += "--> " + entry.getKey() + " : " + entry.getValue() + "\n";
        }
        return str;
    }

    public HttpRequest(String request) throws WrongRequestException {

        request = request.substring(0, request.length()-1);
        int i = request.indexOf('\n');
        String startingStr = request.substring(0, i);
        request = request.substring(i+1);

        i = startingStr.indexOf(' ');
        method = startingStr.substring(0, i);
        startingStr = startingStr.substring(i+1);
        i = startingStr.indexOf(' ');
        path = startingStr.substring(0, i);
        protocol = startingStr.substring(i+1);

        while (!request.isEmpty()) {
            i = request.indexOf('\n');
            String str = request.substring(0, i);
            request = request.substring(i+1);

            i = str.indexOf(':');
            body.put(
                str.substring(0, i),
                str.substring(i + 2)
            );
        }
    }

    public String getMethod()   { return method; }
    public String getPath()     { return path; }
    public String getProtocol() { return protocol; }

    public boolean isGET()  { return method.equals("GET"); }
    public boolean isPOST() { return method.equals("POST"); }

    public String getHeader(String key) throws Exception {
        if (!body.containsKey(key)) {
            throw new Exception("No such header");
        }
        return body.get(key);
    }

    public String getCookie(String key)  throws Exception {
        String[] cookies = getHeader("Cookie").split(";");
        for (var q : cookies) {
            String[] arr = q.strip().split("=");
            if (arr[0].equals(key)) {
                return arr[1];
            }
        }
        throw new Exception("No such cookie");
    }

    public SessionIdentifier getCookie_SessionIdentifier() {
        try {
            return new SessionIdentifier(getCookie("SessionIdentifier"));
        }
        catch (Exception e) {
            return SessionIdentifier.NOSID;
        }
    }

}

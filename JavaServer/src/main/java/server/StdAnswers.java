package server;

import java.io.PrintWriter;

public class StdAnswers {

    public static void NotFound(PrintWriter output) {
        output.println("HTTP/1.1 404 Not Found");
        output.println();
    }

    public static void BadRequest(PrintWriter output) {
        output.println("HTTP/1.1 400 Bad Request");
        output.println();
    }

    public static void MethodNotAllowed(PrintWriter output) {
        output.println("HTTP/1.1 405 Method Not Allowed");
        output.println();
    }

    public static void InternalServerError(PrintWriter output) {
        output.println("HTTP/1.1 500 Internal Server Error");
        output.println();
    }

}

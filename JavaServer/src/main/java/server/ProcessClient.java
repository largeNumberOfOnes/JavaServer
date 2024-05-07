package server;

import initial.MyLogger;
import server.requestProcessor.RequestProcessor;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class ProcessClient implements Runnable {

    private final Socket socket;

    public ProcessClient(Socket socket) {
        this.socket = socket;
    }

    private String readRequest(BufferedReader input) throws IOException {
        try {
            while (!input.ready()) { Thread.sleep(100); }
        }
        catch (InterruptedException e) {
            return null;
        }

        StringBuilder requestStr = new StringBuilder();
        while (input.ready()) {
            requestStr.append(input.readLine());
            requestStr.append("\n");
        }
        return requestStr.toString();
    }

    @Override
    public void run() {

        MyLogger logger = MyLogger.getInstance();

        logger.info("Starting ProcessClient[" + Thread.currentThread().getId() + "]");
        ServerAnswer ans = null;
        try (
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter output = new PrintWriter(socket.getOutputStream())
        ) {
            try {
                String requestStr = readRequest(input);
                logger.info("HTTP request:\n" + requestStr.replace("\n", "\n\t| "));

                HttpRequest request = new HttpRequest(requestStr);
                logger.info("Process HTTP request");
                ans = RequestProcessor.process(request);

            }
            catch (IOException e) {
                ans = ServerAnswer.InternalServerError;
                logger.warning("Error while receiving HTTP request", e);
            }
            catch (WrongRequestException e) {
                ans = ServerAnswer.BadRequest;
                logger.warning("Wrong HTTP request", e);
            }

            logger.info("Send server answer:\n" + ans.toString());
//            System.out.println("ans = " + ans.toString());
            output.println(ans.toString());
//            output.write(ans.toString());
//            output.write("\n");
//            output.flush();
        }
        catch (IOException e) {
            logger.warning("Client connection processing error", e);
        }
        catch (Exception e) {
            logger.warning("Client processing error", e);
        }
        finally {
            logger.info("Closing ProcessClient[" + Thread.currentThread().getId() + "]");
        }
    }
}

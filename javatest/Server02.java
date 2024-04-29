import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import java.io.FileReader;
import java.io.BufferedReader;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server02 {

    static String loadPage() {

        String page = "";
        try(BufferedReader file = new BufferedReader(new FileReader("page.html"))) {
            String tmp = "";
            while ((tmp = file.readLine()) != null) {
                page += tmp + "\n";
            }
            // System.out.println(str);

        }
        catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return page;
    };

    static void mainThreadFunction() {
        String myPage = loadPage();
        int counter = 0;



        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started!");
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                try (
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    PrintWriter output = new PrintWriter(socket.getOutputStream())
                ) {

                    int flag = 0;
                    while (!input.ready()) ;
                    // System.out.println("Client request!");

                    while (input.ready()) {
                        System.out.println(input.readLine());
                    }

                    
                    output.println("HTTP/1.1 200");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    output.println(myPage);
                    output.println();
                    output.println();
                    output.flush();
                    flag++;

                    // output.println("mes1");
                    // output.println();
                    // output.println();
                    // output.flush();
                    
                }
                System.out.println("Client disconnected!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static void wsThreadFunction() {

        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("ws started!");
            
            while (true) {
                Socket socket = serverSocket.accept();

                try (
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    PrintWriter output = new PrintWriter(socket.getOutputStream())
                ) {

                    int flag = 0;
                    boolean flag1 = false;
                    boolean flag2 = false;
                    String str01 = "";
                    while (!input.ready()) ;
                    System.out.println("Client ws request!");

                    while (input.ready()) {
                        String str = input.readLine();
                        if ("Upgrade: websocket" == str) {
                            flag1 = true;
                        }
                        System.out.println(str);
                        if (-1 != str.indexOf("Sec-WebSocket-Key")) {
                            str01 = str.split(" ")[1];
                            flag2 = true;
                        }
                    }

                    if (flag1 == true) {
                        flag1 = false;

                        System.out.println("-------------------------------");
                        // output.println("HTTP/1.1 ");
                        output.println("101 Switching Protocols");
                        output.println("Upgrade: websocket");
                        output.println("Connection: Upgrade");
                        output.println("Sec-WebSocket-Accept: " + AcceptFromKey(str01));
                        output.println();
                        output.println();
                        output.flush();
                    }
                
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String AcceptFromKey(String key) {

        // String guid = "b4f49b36-9bba-4d43-8d5b-ed34d1f534a3";
        // String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        String guid = "6F9619FF-8B86-D011-B42D-00CF4FC964FF";
        key += guid;
        System.out.println(key);
        try {
            // String n = "Iv8io/9s+lYFgZWcXczP8Q==";
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] result = md.digest(key.getBytes(StandardCharsets.UTF_8));

            System.out.println(result);
            StringBuilder hexString = new StringBuilder();
            for (byte b : result) {
                hexString.append(String.format("%02x", b));
            }
            String n = hexString.toString();
            System.out.println(n);
            // Base64 b = new Base64();
            return Base64.getEncoder().encodeToString(result);
            // System.out.println( Base64.getEncoder().encodeToString(result) );
            // System.out.println( Base64.getEncoder().encodeToString(n.getBytes()) );
            // byte[] enc = Base64.encodeBase64(n.getBytes());
            // System.out.println(  );


        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        return "";  
    }

    public static void main(String[] args) {

        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mainThreadFunction();
            }
        });
        mainThread.start();

        Thread wsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                wsThreadFunction();
            }
        });
        // wsThread.start();
        
    }
}

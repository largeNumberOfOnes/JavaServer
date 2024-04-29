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

import java.io.File;

// import javax.websocket.OnMessage;
// import javax.websocket.server.ServerEndpoint;

// class WebSocket {
//     @OnMessage
//     public String handleTextMessage(String message) {
//         System.out.println("New Text Message Received");
//         return message;
//     }
 
//     @OnMessage(maxMessageSize = 1024000)
//     public byte[] handleBinaryMessage(byte[] buffer) {
//     // public byte&#91;] handleBinaryMessage(byte&#91;] buffer) {
//         System.out.println("New Binary Message Received");
//         return buffer;
//     }
// }

public class Server03 {

    //? ###################################################################

    static String loadPage() {

        String page = "";
        try(BufferedReader file = new BufferedReader(new FileReader("page.html"))) {
            String tmp = "";
            while ((tmp = file.readLine()) != null) {
                page += tmp + "\n";
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return page;
    };

    static void sendPage(PrintWriter output) {

        String myPage = loadPage();

        output.println("HTTP/1.1 200");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();
        // output.print("data:");
        output.println(myPage);
        output.println();
        output.println();
        output.flush();
    }

    //? ###################################################################

    static String loadIcon() {
        String iconAddress = "favicon.png";
        try (var fst = new BufferedReader(new FileReader(iconAddress))) {
            var file = new File(iconAddress);
            byte[] buffer = new byte[(int)file.length()+1];
            // System.out.println(file.length());
            int q = 0;
            // file.read(buffer);
            while ((buffer[q++] = (byte)fst.read()) != -1) ;

            String str = Base64.getEncoder().encodeToString(buffer);
            // System.out.println(str);
            return str;

        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return null;

    }

    static void sendIcon(PrintWriter output) {

        String myIcon = loadIcon();

        output.println("HTTP/1.1 200");
        // output.println("Content-Type: image/ico; charset=utf-8");
        output.println("Content-Type: image/png; charset=utf-8");
        output.println();
        // output.print("data:image/png;base64,");
        // output.print("data:base64,");
        output.println(myIcon);
        output.println();
        output.println();
        output.flush();
    }

    //? ###################################################################

    static void sendSwitchingAccept(PrintWriter output, String request) {
        String key = request.substring(
            request.indexOf("Sec-WebSocket-Key") + "Sec-WebSocket-Key".length() + 2
        );
        key = key.substring(0, key.indexOf("\n"));
        System.out.println("------>" + key);

        // output.println("HTTP/1.1 404 Not Found");
        output.println("HTTP/1.1 101 Switching Protocols");
        output.println("Upgrade: websocket");
        output.println("Connection: Upgrade");
        output.println("Sec-WebSocket-Accept: " + AcceptFromKey(key));
        output.println();
        output.flush();
    }

    //? ###################################################################

    enum Method { NONE, GET }
    record Header(Method method, String dir, String prot) {}
    enum RequestType {
        GET_MAIN_PAGE,
        GET_ICON,
        SWITCH_TO_WEBSOCKET,
        NONE
    }

    static Header parseHeader(String header) throws Exception {

        String[] typarr = header.split(" ");
        
        Method method;
        if (typarr[0].equals("GET")) {
            method = Method.GET;
        } else {
            method = Method.NONE;
        };
        String dir = typarr[1];
        if (!typarr[2].equals("HTTP/1.1")) {
            throw new Exception("Unrecognized protocol: |" + typarr[2] + "|");
        }

        return new Header(method, dir, typarr[2]);
    }

    static RequestType recognizeRequest(String request) throws Exception {

        String[] reqarr = request.split("\n");
        for (String q : reqarr) {
            // System.out.println("-->" + q);
        }

        Header header = parseHeader(reqarr[0]);

        if (header.method == Method.GET) {
            if (header.dir.equals("/") && request.indexOf("Accept: text/html") != -1) {
                return RequestType.GET_MAIN_PAGE;
            } else if (header.dir.equals("/favicon.ico")) {
                return RequestType.GET_ICON;
            } else if (request.indexOf("Upgrade: websocket") != -1) {
                return RequestType.SWITCH_TO_WEBSOCKET;
            } else {
                return RequestType.NONE;
            }
        // } else if (header.method) {
        //     return RequestType.SWITCH_TO_WEBSOCKET;    
        // }
        } else {
            System.out.println("-->" + header);
        }
        
        return RequestType.NONE;
    }

    //? ###################################################################

    static void processReqest(
        RequestType type,
        String request,
        PrintWriter output,
        BufferedReader input
    ) throws Exception {
        
        switch (type) {
            case GET_MAIN_PAGE:
                sendPage(output);
                break;
            case GET_ICON:
                sendIcon(output);
                break;
            case SWITCH_TO_WEBSOCKET:
                sendSwitchingAccept(output, request);
                // System.out.println("No handler");
                while (true) {
                    output.println("hhi");
                    System.out.println("-->>mewr");
                    if (input.ready()) {
                        while (input.ready()) {
                            System.out.println(input.readLine());
                        }
                        System.out.println("-->>disc");
                        break;
                    }
                    Thread. sleep(3000);
                }
                break;
            default:
                throw new Exception("No such handler for: " + type);
        }

    }

    //? ##################################################################

    static void mainThreadFunction() {
        int counter = 0;

// 204 No Content

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started:");
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("---Client connected!");

                try (
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    PrintWriter output = new PrintWriter(socket.getOutputStream())
                ) {
                    while (!input.ready()) ;



                    // String request = "";
                    String request = "";
                    while (input.ready()) {
                        // request += input.readLine() + "\n";
                        request += input.readLine() + "\n";
                    }
                    System.out.println(request);

                    RequestType reqType = RequestType.NONE;
                    try {
                        reqType = recognizeRequest(request);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    System.out.println(reqType);

                    try {
                        processReqest(reqType, request, output, input);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    // sendPage(output);
                    
                }
                System.out.println("---Client disconnected!");
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
        // String guid = "6F9619FF-8B86-D011-B42D-00CF4FC964FF";
        String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        key += guid;
        System.out.println(key);
        try {
            // String n = "Iv8io/9s+lYFgZWcXczP8Q==";
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] result = md.digest(key.getBytes(StandardCharsets.UTF_8));
            // byte[] result = md.digest(key.getBytes());

            System.out.println(result);
            StringBuilder hexString = new StringBuilder();
            String str = "";
            for (byte b : result) {
                hexString.append(String.format("%02x", b));
                // hexString.append((char)b);
                str += (char)b;
                // hexString.append(b);
            }
            String n = hexString.toString();
            System.out.println("n: " + n);
            System.out.println(str);
            System.out.println(Base64.getEncoder().encodeToString(result));
            // str  = Base64.getEncoder().encodeToString(str);
            // System.out.println(str);
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

        // loadIcon();
        // System.out.println(AcceptFromKey("ViFipDSLZ5fyHuVf3PFDBQ=="));


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

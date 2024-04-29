import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
 
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.*;
 
public class Program {

    // static int 
 
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler1());
        server.createContext("/new", new MyHandler2());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("111");
    }
 
    static class MyHandler1 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = """
                <html>
                    Hello, World 7!
                    <br>
                    <a href="/new">link</a>
                </html>
            """;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    static class MyHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = """
                <html>
                    This is new context!
                    <br>
                    <a href="/">link</a>
                </html>
            """;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}




// package letscode;

// import java.io.IOException;
// import java.net.InetSocketAddress;
// import java.nio.ByteBuffer;
// import java.nio.channels.AsynchronousServerSocketChannel;
// import java.nio.channels.AsynchronousSocketChannel;
// import java.util.Arrays;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.TimeoutException;

// public class Program {
//     public static void main(String[] args) {
//         new Server().bootstrap();
//     }
// }

// class Server {
//     private final static int BUFFER_SIZE = 256;
//     private AsynchronousServerSocketChannel server;

//     private final static String HEADERS =
//                     "HTTP/1.1 200 OK\n" +
//                     "Server: naive\n" +
//                     "Content-Type: text/html\n" +
//                     "Content-Length: %s\n" +
//                     "Connection: close\n\n";

//     public void bootstrap() {
//         try {
//             server = AsynchronousServerSocketChannel.open();
//             server.bind(new InetSocketAddress("127.0.0.1", 8080));

//             while (true) {
//                 Future<AsynchronousSocketChannel> future = server.accept();
//                 handleClient(future);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         } catch (ExecutionException e) {
//             e.printStackTrace();
//         } catch (TimeoutException e) {
//             e.printStackTrace();
//         }
//     }

//     private void handleClient(Future<AsynchronousSocketChannel> future)
//             throws InterruptedException, ExecutionException, TimeoutException, IOException {
//         System.out.println("new client thread");

//         AsynchronousSocketChannel clientChannel = future.get(30, TimeUnit.SECONDS);

//         while (clientChannel != null && clientChannel.isOpen()) {
//             ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//             StringBuilder builder = new StringBuilder();
//             boolean keepReading = true;

//             while (keepReading) {
//                 clientChannel.read(buffer).get();

//                 int position = buffer.position();
//                 keepReading = position == BUFFER_SIZE;

//                 byte[] array = keepReading
//                         ? buffer.array()
//                         : Arrays.copyOfRange(buffer.array(), 0, position);

//                 builder.append(new String(array));
//                 buffer.clear();
//             }

//             String body = "<html><body><h1>Hello, naive</h1></body></html>";
//             String page = String.format(HEADERS, body.length()) + body;
//             ByteBuffer resp = ByteBuffer.wrap(page.getBytes());
//             clientChannel.write(resp);

//             clientChannel.close();
//         }
//     }
// }

// import java.io.FileOutputStream;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

class Test {
    @OnMessage
    public String handleTextMessage(String message) {
        System.out.println("New Text Message Received");
        return message;
    }
 
    @OnMessage(maxMessageSize = 1024000)
    public byte[] handleBinaryMessage(byte[] buffer) {
    // public byte&#91;] handleBinaryMessage(byte&#91;] buffer) {
        System.out.println("New Binary Message Received");
        return buffer;
    }
}

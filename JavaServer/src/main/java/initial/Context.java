package initial;

public class Context {

    // Server
    public static final int port = 8080;
    public static final String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    public static final String basePath = "/home/dt/Documents/JavaServer/WebPage";
    public static final String mainPage = "/public/index.html";
    public static final String afterLoginPage = "/internal/index.html";
//    public static final String publicFolder = "/public/index.html";
    public static boolean isPublic(String path) {
        try {
            return path.startsWith("/public");
        }
        catch (Exception e) {
            return false;
        }
    }

    // Data Base
    public static final String DBURL = "jdbc:postgresql://192.168.56.101:5432/postgres";
    public static final String DBUser = "vudb";
    public static final String DBPass = "pass";
}

package server;

public class SessionIdentifier {

    private String id;

    public SessionIdentifier(String login, String pass) {
        id = login + pass;
    }
    public SessionIdentifier(String sessionIdentifier) {
        id = sessionIdentifier;
    }
    public SessionIdentifier() {
        id = null;
    }

    public boolean isNull() {
        return id == null;
    }

    @Override
    public String toString() {
        return id;
    }

    public static SessionIdentifier NOSID = new SessionIdentifier();
}

package server;

public class SessionIdentifier {

    private String id;
    private String login = "untiledUser";

    public SessionIdentifier(String login, String pass) {
        id = createAsString(login, pass);
        this.login = login;
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

    public static String createAsString(String login, String pass) {
        return login + "^" + pass;
    }
    public static SessionIdentifier NOSID = new SessionIdentifier();

    public String getLogin() {
        return id.split("\\^")[0];
    }

    @Override
    public boolean equals(Object id) {
        if (id instanceof SessionIdentifier) {
            return id.toString().equals(this.id);
        }
        return false;
    }
}

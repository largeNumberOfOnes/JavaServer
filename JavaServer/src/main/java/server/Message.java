package server;

public class Message {

    String date;
    String time;
    String login;
    String type;
    String mes;

    public Message(String date, String time, String login, String type, String mes) {
        this.date  = date;
        this.time  = time;
        this.login = login;
        this.type  = type;
        this.mes   = mes;
    }

}

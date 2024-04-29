package server;

public class WrongRequestException extends Exception {

    public WrongRequestException() {
        super("Wrong request");
    }
}

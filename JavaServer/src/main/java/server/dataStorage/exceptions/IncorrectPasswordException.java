package server.dataStorage.exceptions;

public class IncorrectPasswordException extends DataStorageException {

    public IncorrectPasswordException(String pass) {
        super("Incorrect password [%s]".formatted(pass));
    }
}

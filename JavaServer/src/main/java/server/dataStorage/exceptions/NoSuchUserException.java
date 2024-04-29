package server.dataStorage.exceptions;

public class NoSuchUserException extends DataStorageException {

    public NoSuchUserException(String login) {
        super("Error: no such user [%s]".formatted(login));
    }
}

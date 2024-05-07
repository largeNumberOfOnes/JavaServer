package server.dataStorage.exceptions;

public class UserAlreadyExistsException extends DataStorageException {

    public UserAlreadyExistsException(String login) {
        super("Error: user [%s] already exists".formatted(login));
    }
}

package server.dataStorage.exceptions;

import server.SessionIdentifier;

public class NoSuchSessionIdentifier extends DataStorageException {

    public NoSuchSessionIdentifier(SessionIdentifier id) {
        super("Error: user such session identifier [%s]".formatted(id.toString()));
    }
}

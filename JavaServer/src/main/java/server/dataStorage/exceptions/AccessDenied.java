package server.dataStorage.exceptions;

public class AccessDenied extends DataStorageException {

    public AccessDenied(String file) {
        super("Access to file %s denied".formatted(file));
    }

}

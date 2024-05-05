package server.dataStorage.exceptions;

public class ResourceNotFound extends DataStorageException {

    public ResourceNotFound(String file) {
        super("Resource %s not found".formatted(file));
    }

}

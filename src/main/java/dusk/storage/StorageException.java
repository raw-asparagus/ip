package dusk.storage;

/**
 * Represents an exception specifically related to storage operations,
 * including loading and saving task data.
 */
public class StorageException extends Exception {

    /**
     * Constructs a StorageException with the specified detail message.
     *
     * @param message the detail message providing more information about the exception
     */
    public StorageException(String message) {
        super(message);
    }
}
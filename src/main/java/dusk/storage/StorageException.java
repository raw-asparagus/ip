package dusk.storage;

import dusk.DuskException;
import dusk.DuskExceptionType;

/**
 * Indicates an exception that occurs during storage operations,
 * including loading and saving tasks.
 */
public class StorageException extends DuskException {

    /**
     * Constructs a StorageException with the specified detail message.
     *
     * @param message the detail message describing the exception
     */
    public StorageException(String message) {
        super(message, DuskExceptionType.STORAGE_ERROR);
    }
}

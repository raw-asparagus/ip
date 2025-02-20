package dusk.command;

/**
 * Enumerates all supported command types in the application.
 */
public enum CommandType {
    LIST,
    FIND,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT;

    /**
     * Converts a string to its corresponding CommandType enum,
     * or returns null if not recognized.
     *
     * @param command the input command string
     * @return corresponding CommandType or null if not found
     */
    public static CommandType fromString(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
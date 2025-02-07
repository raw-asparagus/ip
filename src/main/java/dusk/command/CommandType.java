package dusk.command;

/**
 * Enumerates the different types of commands that can be recognized
 * and processed by the application.
 */
public enum CommandType {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT;

    /**
     * Converts a string command into its corresponding {@code CommandType}.
     *
     * @param command the string representing a command (e.g. "list", "to-do")
     * @return the matching CommandType, or null if the command is not recognized
     */
    public static CommandType fromString(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
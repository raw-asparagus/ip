package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Parser class, ensuring commands are parsed correctly.
 */
class ParserTest {

    private ConsoleIO consoleIO;
    private Storage storage;
    private TaskList tasks;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        consoleIO = new ConsoleIO(System.in, System.out);
        storage = new Storage();
        tasks = new TaskList();
    }

    /**
     * Tests that parsing an empty input string throws an InputException.
     */
    @Test
    void parse_emptyInput_throwsInputException() {
        assertThrows(InputException.class, () ->
                        Parser.parse(consoleIO, storage, tasks, ""),
                "Parser should throw InputException for empty input."
        );
    }

    /**
     * Tests that parsing an unrecognized command string throws an InputException.
     */
    @Test
    void parse_invalidCommand_throwsInputException() {
        assertThrows(InputException.class, () ->
                        Parser.parse(consoleIO, storage, tasks, "unknowncommand"),
                "Parser should throw InputException for invalid or unrecognized commands."
        );
    }

    /**
     * Tests that the "list" command returns a ListCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_listCommand_returnsListCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "list");
        assertInstanceOf(ListCommand.class, command, "Parser should return a ListCommand for 'list' input.");
    }

    /**
     * Tests that the "todo" command returns a CreateTodoCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_todoCommand_returnsCreateTodoCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "todo Buy groceries");
        assertInstanceOf(CreateTodoCommand.class, command,
                "Parser should return a CreateTodoCommand for 'todo' input.");
    }

    /**
     * Tests that the "deadline" command returns a CreateDeadlineCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_deadlineCommand_returnsCreateDeadlineCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "deadline Finish assignment /by 2023-10-10 1300");
        assertInstanceOf(CreateDeadlineCommand.class, command,
                "Parser should return a CreateDeadlineCommand for 'deadline' input with /by date.");
    }

    /**
     * Tests that the "event" command returns a CreateEventCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_eventCommand_returnsCreateEventCommand() throws InputException {
        Command command = Parser.parse(
                consoleIO,
                storage,
                tasks,
                "event Team meeting /from 2023-12-01 /to 2023-12-01 1500"
        );
        assertInstanceOf(CreateEventCommand.class, command,
                "Parser should return a CreateEventCommand for 'event' input with /from and /to dates.");
    }

    /**
     * Tests that parsing an invalid date/time format throws an InputException.
     */
    @Test
    void parse_invalidDateTime_throwsInputException() {
        assertThrows(InputException.class, () ->
                        Parser.parse(consoleIO, storage, tasks, "deadline Something /by 2023-99-99"),
                "Parser should throw InputException on invalid date/time format."
        );
    }

    /**
     * Tests that the "mark" command returns a MarkCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_markCommand_returnsMarkCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "mark 1");
        assertInstanceOf(MarkCommand.class, command, "Parser should return a MarkCommand for 'mark' input.");
    }

    /**
     * Tests that the "unmark" command returns a MarkCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_unmarkCommand_returnsMarkCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "unmark 1");
        assertInstanceOf(MarkCommand.class, command, "Parser should return a MarkCommand for 'unmark' input.");
    }

    /**
     * Tests that the "delete" command returns a DeleteCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parse_deleteCommand_returnsDeleteCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "delete 1");
        assertInstanceOf(DeleteCommand.class, command,
                "Parser should return a DeleteCommand for 'delete' input.");
    }
}
package dusk.command;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Tests for the Parser class, ensuring commands are parsed correctly.
 */
class ParserTest {

    private DuskIO duskIo;
    private Storage storage;
    private TaskList taskList;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        duskIo = new DuskIO(System.in, System.out);
        storage = new Storage();
        taskList = new TaskList();
    }

    /**
     * Tests that parsing an empty input string throws an InputException.
     */
    @Test
    void parseEmptyInputThrowsInputException() {
        assertThrows(
                InputException.class,
                () -> Parser.parse(duskIo, storage, taskList, ""),
                "Parser should throw InputException for empty input."
        );
    }

    /**
     * Tests that parsing an unrecognized command string throws an InputException.
     */
    @Test
    void parseInvalidCommandThrowsInputException() {
        assertThrows(
                InputException.class,
                () -> Parser.parse(duskIo, storage, taskList, "unknowncommand"),
                "Parser should throw InputException for invalid or unrecognized commands."
        );
    }

    /**
     * Tests that the "list" command returns a ListCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseListCommandReturnsListCommand() throws InputException {
        Command command = Parser.parse(duskIo, storage, taskList, "list");
        assertInstanceOf(
                ListCommand.class,
                command,
                "Parser should return a ListCommand for 'list' input."
        );
    }

    /**
     * Tests that the "todo" command returns a CreateTodoCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseTodoCommandReturnsCreateTodoCommand() throws InputException {
        Command command = Parser.parse(duskIo, storage, taskList, "todo Buy groceries");
        assertInstanceOf(
                CreateTodoCommand.class,
                command,
                "Parser should return a CreateTodoCommand for 'todo' input."
        );
    }

    /**
     * Tests that the "deadline" command returns a CreateDeadlineCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseDeadlineCommandReturnsCreateDeadlineCommand() throws InputException {
        Command command = Parser.parse(
                duskIo,
                storage,
                taskList,
                "deadline Finish assignment /by 2023-10-10 1300"
        );
        assertInstanceOf(
                CreateDeadlineCommand.class,
                command,
                "Parser should return a CreateDeadlineCommand for 'deadline' input with /by date."
        );
    }

    /**
     * Tests that the "event" command returns a CreateEventCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseEventCommandReturnsCreateEventCommand() throws InputException {
        Command command = Parser.parse(
                duskIo,
                storage,
                taskList,
                "event Team meeting /from 2023-12-01 /to 2023-12-01 1500"
        );
        assertInstanceOf(
                CreateEventCommand.class,
                command,
                "Parser should return a CreateEventCommand for 'event' input with /from and /to dates."
        );
    }

    /**
     * Tests that parsing an invalid date/time format throws an InputException.
     */
    @Test
    void parseInvalidDateTimeThrowsInputException() {
        assertThrows(
                InputException.class,
                () -> Parser.parse(duskIo, storage, taskList, "deadline Something /by 2023-99-99"),
                "Parser should throw InputException on invalid date/time format."
        );
    }

    /**
     * Tests that the "mark" command returns a MarkCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseMarkCommandReturnsMarkCommand() throws InputException {
        Command command = Parser.parse(duskIo, storage, taskList, "mark 1");
        assertInstanceOf(
                MarkCommand.class,
                command,
                "Parser should return a MarkCommand for 'mark' input."
        );
    }

    /**
     * Tests that the "unmark" command returns a MarkCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseUnmarkCommandReturnsMarkCommand() throws InputException {
        Command command = Parser.parse(duskIo, storage, taskList, "unmark 1");
        assertInstanceOf(
                MarkCommand.class,
                command,
                "Parser should return a MarkCommand for 'unmark' input."
        );
    }

    /**
     * Tests that the "delete" command returns a DeleteCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseDeleteCommandReturnsDeleteCommand() throws InputException {
        Command command = Parser.parse(duskIo, storage, taskList, "delete 1");
        assertInstanceOf(
                DeleteCommand.class,
                command,
                "Parser should return a DeleteCommand for 'delete' input."
        );
    }

    /**
     * Tests that the "find" command returns a FindCommand instance.
     *
     * @throws InputException if there is an invalid input to the command.
     */
    @Test
    void parseFindCommandReturnsFindCommand() throws InputException {
        Command command = Parser.parse(duskIo, storage, taskList, "find homework");
        assertInstanceOf(
                FindCommand.class,
                command,
                "Parser should return a FindCommand for 'find' input."
        );
    }
}

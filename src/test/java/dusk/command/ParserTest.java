package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private ConsoleIO consoleIO;
    private Storage storage;
    private TaskList tasks;

    @BeforeEach
    void setUp() {
        // Replace with your real or mock objects as needed
        consoleIO = new ConsoleIO(System.in, System.out);
        storage = new Storage();
        tasks = new TaskList();
    }

    @Test
    void parse_emptyInput_throwsInputException() {
        assertThrows(InputException.class, () ->
                        Parser.parse(consoleIO, storage, tasks, ""),
                "Parser should throw InputException for empty input."
        );
    }

    @Test
    void parse_invalidCommand_throwsInputException() {
        assertThrows(InputException.class, () ->
                        Parser.parse(consoleIO, storage, tasks, "unknowncommand"),
                "Parser should throw InputException for invalid or unrecognized commands."
        );
    }

    @Test
    void parse_listCommand_returnsListCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "list");
        assertInstanceOf(ListCommand.class, command, "Parser should return a ListCommand for 'list' input.");
    }

    @Test
    void parse_todoCommand_returnsCreateTodoCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "todo Buy groceries");
        assertInstanceOf(CreateTodoCommand.class, command, "Parser should return a CreateTodoCommand for 'todo' input.");
    }

    @Test
    void parse_deadlineCommand_returnsCreateDeadlineCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "deadline Finish assignment /by 2023-10-10 1300");
        assertInstanceOf(CreateDeadlineCommand.class, command, "Parser should return a CreateDeadlineCommand for 'deadline' input with /by date.");
    }

    @Test
    void parse_eventCommand_returnsCreateEventCommand() throws InputException {
        Command command = Parser.parse(
                consoleIO,
                storage,
                tasks,
                "event Team meeting /from 2023-12-01 /to 2023-12-01 1500"
        );
        assertInstanceOf(CreateEventCommand.class, command, "Parser should return a CreateEventCommand for 'event' input with /from and /to dates.");
    }

    @Test
    void parse_invalidDateTime_throwsInputException() {
        // e.g. invalid format
        assertThrows(InputException.class, () ->
                        Parser.parse(consoleIO, storage, tasks, "deadline Something /by 2023-99-99"),
                "Parser should throw InputException on invalid date/time format."
        );
    }

    @Test
    void parse_markCommand_returnsMarkCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "mark 1");
        assertInstanceOf(MarkCommand.class, command, "Parser should return a MarkCommand for 'mark' input.");
    }

    @Test
    void parse_unmarkCommand_returnsMarkCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "unmark 1");
        assertInstanceOf(MarkCommand.class, command, "Parser should return a MarkCommand for 'unmark' input.");
    }

    @Test
    void parse_deleteCommand_returnsDeleteCommand() throws InputException {
        Command command = Parser.parse(consoleIO, storage, tasks, "delete 1");
        assertInstanceOf(DeleteCommand.class, command, "Parser should return a DeleteCommand for 'delete' input.");
    }
}

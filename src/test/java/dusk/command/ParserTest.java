package dusk.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dusk.ui.DuskIO;
import dusk.storage.Storage;
import dusk.task.TaskList;

/**
 * Test cases for verifying the functionality of the Parser component.
 */
public class ParserTest {

    private DuskIO duskIO;
    private Storage storage;
    private TaskList tasks;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        duskIO = new DuskIO(new StringReader(""), new StringWriter());
        storage = new Storage();
        tasks = new TaskList();
    }

    /**
     * Verifies that parsing a valid todo command works correctly.
     */
    @Test
    public void parseTodoCommandSuccess() {
        assertDoesNotThrow(() -> {
            Command cmd = Parser.parse(duskIO, storage, tasks, "todo Read book");
            assertInstanceOf(CreateTodoCommand.class, cmd);
        });
    }

    /**
     * Verifies that parsing a valid deadline command works correctly.
     */
    @Test
    public void parseDeadlineCommandSuccess() {
        assertDoesNotThrow(() -> {
            Command cmd = Parser.parse(duskIO, storage, tasks,
                    "deadline Submit report /by 2024-03-20 1400");
            assertInstanceOf(CreateDeadlineCommand.class, cmd);
        });
    }

    /**
     * Verifies that parsing a valid event command works correctly.
     */
    @Test
    public void parseEventCommandSuccess() {
        assertDoesNotThrow(() -> {
            Command cmd = Parser.parse(duskIO, storage, tasks,
                    "event Team meeting /from 2024-03-20 1400 /to 2024-03-20 1500");
            assertInstanceOf(CreateEventCommand.class, cmd);
        });
    }

    /**
     * Verifies that parsing a valid list command works correctly.
     */
    @Test
    public void parseListCommandSuccess() {
        assertDoesNotThrow(() -> {
            Command cmd = Parser.parse(duskIO, storage, tasks, "list");
            assertInstanceOf(ListCommand.class, cmd);
        });
    }

    /**
     * Verifies that a missing description triggers an InputException.
     */
    @Test
    public void parseCommandNoDescriptionThrowsInputException() {
        assertThrows(InputException.class, () -> Parser.parse(duskIO, storage, tasks, "todo"));
    }

    /**
     * Verifies that a deadline command with an invalid datetime format triggers an InputException.
     */
    @Test
    public void parseDeadlineInvalidDateTimeThrowsInputException() {
        assertThrows(InputException.class, () -> Parser.parse(duskIO, storage, tasks,
                "deadline Submit report /by invalid-date"));
    }

    /**
     * Verifies that an event command missing required flags triggers an InputException.
     */
    @Test
    public void parseEventMissingFlagsThrowsInputException() {
        assertThrows(InputException.class, () -> Parser.parse(duskIO, storage, tasks,
                "event Team meeting /from 2024-03-20 1400"));
    }

    /**
     * Verifies that an invalid command type triggers an InputException.
     */
    @Test
    public void parseInvalidCommandTypeThrowsInputException() {
        assertThrows(InputException.class, () -> Parser.parse(duskIO, storage, tasks, "invalid command"));
    }

    /**
     * Verifies that parsing a valid find command works correctly.
     */
    @Test
    public void parseFindCommandSuccess() {
        assertDoesNotThrow(() -> {
            Command cmd = Parser.parse(duskIO, storage, tasks, "find book");
            assertInstanceOf(FindCommand.class, cmd);
        });
    }

    /**
     * Verifies that mark and unmark commands are parsed correctly.
     */
    @Test
    public void parseMarkUnmarkCommandsSuccess() {
        assertDoesNotThrow(() -> {
            Command markCmd = Parser.parse(duskIO, storage, tasks, "mark 1");
            Command unmarkCmd = Parser.parse(duskIO, storage, tasks, "unmark 1");
            assertInstanceOf(MarkCommand.class, markCmd);
            assertInstanceOf(MarkCommand.class, unmarkCmd);
        });
    }
}

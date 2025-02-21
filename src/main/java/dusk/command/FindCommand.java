package dusk.command;

import java.io.IOException;

import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Command that searches through tasks for descriptions matching
 * a specified keyword (both exact and partial matches).
 */
public class FindCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final String keyword;

    /**
     * Constructs a FindCommand.
     *
     * @param tasks   the current task list
     * @param duskIO  the I/O interface
     * @param keyword the keyword to search for
     */
    public FindCommand(TaskList tasks, DuskIO duskIO, String keyword) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.keyword = keyword;
    }

    @Override
    public void execute() throws IOException, TaskListException {
        // Search for exact matches.
        TaskList exactMatches = tasks.search(keyword, null, null, null);
        // Search for partial matches.
        TaskList partialMatches = tasks.search(keyword.toLowerCase(), null, null, null);

        TaskList combinedResults = new TaskList();

        // Add exact matches.
        for (int i = 0; i < exactMatches.size(); i++) {
            combinedResults.addTask(exactMatches.getTask(i));
        }

        // Add partial matches not already included.
        for (int i = 0; i < partialMatches.size(); i++) {
            Task task = partialMatches.getTask(i);
            boolean isNewMatch = true;
            for (int j = 0; j < exactMatches.size(); j++) {
                if (task.equals(exactMatches.getTask(j))) {
                    isNewMatch = false;
                    break;
                }
            }
            if (isNewMatch) {
                combinedResults.addTask(task);
            }
        }

        if (combinedResults.isEmpty()) {
            duskIO.print("No matching tasks found!");
        } else {
            String header = "Here are the matching tasks in your list:";
            String[] messages = new String[combinedResults.size() + 1];
            messages[0] = header;
            for (int i = 0; i < combinedResults.size(); i++) {
                messages[i + 1] = (i + 1) + ". " + combinedResults.getTask(i);
            }
            duskIO.print(messages);
        }
    }
}

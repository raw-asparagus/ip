package dusk.command;

import java.io.IOException;

import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Represents a command that searches the task list for tasks whose
 * descriptions contain a given keyword, including partial matches.
 */
public class FindCommand extends Command {
    private final TaskList tasks;
    private final DuskIO duskIO;
    private final String keyword;

    // Constructor remains the same
    public FindCommand(TaskList tasks, DuskIO duskIO, String keyword) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.keyword = keyword;
    }

    /**
     * Executes the find command: searches for exact and partial matching tasks and prints them.
     *
     * @throws IOException       if an I/O error occurs while printing to the console
     * @throws TaskListException if an error occurs while retrieving tasks
     */
    @Override
    public void execute() throws IOException, TaskListException {
        // First, get exact matches using the existing search method
        TaskList exactMatches = tasks.search(keyword, null, null, null);

        // Then, get partial matches by searching for tasks containing the keyword
        TaskList partialMatches = tasks.search(keyword.toLowerCase(), null, null, null);

        // Remove exact matches from partial matches to avoid duplicates
        TaskList combinedResults = new TaskList();

        // Add exact matches first
        for (int i = 0; i < exactMatches.size(); i++) {
            combinedResults.addTask(exactMatches.getTask(i));
        }

        // Add partial matches that aren't already included
        for (int i = 0; i < partialMatches.size(); i++) {
            Task task = partialMatches.getTask(i);
            boolean isNewMatch = true;

            // Check if this task is already in the exact matches
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
                Task task = combinedResults.getTask(i);
                messages[i + 1] = (i + 1) + "." + task;
            }
            duskIO.print(messages);
        }
    }
}

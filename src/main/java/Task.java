public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    // Mutators
    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    // Accessors
    public String getName() {
        return description;
    }

    public boolean getDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return "[" + (getDone() ? "X" : " ") + "] " + description;
    }
}

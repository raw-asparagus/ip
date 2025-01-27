public class Task {
    protected String desc;
    protected boolean isDone;

    public Task(String desc) {
        this.desc = desc;
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
        return desc;
    }

    public boolean getDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return "[" + (getDone() ? "X" : " ") + "] " + desc;
    }
}

import java.io.IOException;

public interface Command {
    void execute() throws DuskException, InputException, IOException;
}
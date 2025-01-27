import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ConsoleIO implements Closeable {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ConsoleIO(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String readLine() throws IOException {
        return reader.readLine().trim();
    }

    public void print(String... messages) throws IOException {
        printLine();
        for (String message : messages) {
            writer.write("\t " + message + "\n");
        }
        printLine();
        writer.flush();
    }

    private void printLine() throws IOException {
        writer.write("\t" + "_".repeat(60) + "\n");
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
    }
}
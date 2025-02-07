package dusk.ui;

import java.io.*;

public class ConsoleIO implements Closeable {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    // Constructor
    public ConsoleIO(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String readLine() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        return line.trim();
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

    public void debugPrint(String message) throws IOException {
        writer.write(message + "\n");
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
    }
}

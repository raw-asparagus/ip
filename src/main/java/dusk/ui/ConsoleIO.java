package dusk.ui;

import java.io.*;

/**
 * Provides console-based input/output functionality, including reading lines
 * from an input stream and writing messages to an output stream.
 */
public class ConsoleIO implements Closeable {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    /**
     * Constructs a ConsoleIO instance with the specified input and output streams.
     *
     * @param in  the InputStream used to create a BufferedReader
     * @param out the OutputStream used to create a BufferedWriter
     */
    public ConsoleIO(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    /**
     * Reads a line of text from the console input, trimming any leading and trailing spaces.
     *
     * @return the trimmed line of text, or {@code null} if the end of the stream is reached
     * @throws IOException if an I/O error occurs while reading
     */
    public String readLine() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        return line.trim();
    }

    /**
     * Prints one or more messages to the console, each prefixed with a tab and followed by a new line.
     *
     * @param messages an array of messages to be printed
     * @throws IOException if an I/O error occurs while writing
     */
    public void print(String... messages) throws IOException {
        printLine();
        for (String message : messages) {
            writer.write("\t " + message + "\n");
        }
        printLine();
        writer.flush();
    }

    /**
     * Writes a separator line composed of underscores, preceded by a tab character.
     *
     * @throws IOException if an I/O error occurs while writing
     */
    private void printLine() throws IOException {
        writer.write("\t" + "_".repeat(60) + "\n");
    }

    /**
     * Writes a debug message to the console output stream, ending with a new line.
     *
     * @param message the message to be written
     * @throws IOException if an I/O error occurs while writing
     */
    public void debugPrint(String message) throws IOException {
        writer.write(message + "\n");
        writer.flush();
    }

    /**
     * Closes both the reader and writer resources associated with this instance.
     *
     * @throws IOException if an I/O error occurs while closing the resources
     */
    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
    }
}

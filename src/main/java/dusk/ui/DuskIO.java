package dusk.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Objects;

/**
 * Provides console-based input/output functionality.
 */
public class DuskIO implements Closeable {

    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    /**
     * Constructs a DuskIO instance with the specified input and output streams.
     *
     * @param inputStream  the InputStream used for input operations
     * @param outputStream the OutputStream used for output operations
     */
    @Deprecated
    public DuskIO(InputStream inputStream, OutputStream outputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * Constructs a DuskIO instance with the specified reader and writer.
     *
     * @param reader the Reader used for input operations
     * @param writer the Writer used for output operations
     * @throws NullPointerException if either reader or writer is null
     */
    public DuskIO(Reader reader, Writer writer) {
        this.bufferedReader = new BufferedReader(Objects.requireNonNull(reader, "Reader cannot be null"));
        this.bufferedWriter = new BufferedWriter(Objects.requireNonNull(writer, "Writer cannot be null"));
    }

    /**
     * Prints one or more messages to the output, each prefixed with a tab and followed by a newline.
     *
     * @param messages an array of messages to be printed
     * @throws IOException if an I/O error occurs during writing
     */
    public void print(String... messages) throws IOException {
        for (String message : messages) {
            bufferedWriter.write("\t" + message);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
    }

    /**
     * Closes the input and output resources.
     *
     * @throws IOException if an I/O error occurs during closing
     */
    @Override
    public void close() throws IOException {
        bufferedReader.close();
        bufferedWriter.close();
    }
}

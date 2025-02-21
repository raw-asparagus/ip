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

/**
 * Provides console-based input/output functionality, including reading lines
 * from an input stream and writing messages to an output stream.
 */
public class DuskIO implements Closeable {

    /**
     * Buffered reader for handling console input.
     */
    private final BufferedReader bufferedReader;

    /**
     * Buffered writer for handling console output.
     */
    private final BufferedWriter bufferedWriter;

    /**
     * Constructs a DuskIO instance with the specified input and output streams.
     * Deprecated, for console I/O
     *
     * @param inputStream  the InputStream used to create a BufferedReader
     * @param outputStream the OutputStream used to create a BufferedWriter
     */
    public DuskIO(InputStream inputStream, OutputStream outputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * Constructs a new DuskIO instance with the specified reader and writer.
     *
     * @param reader the Reader used for input operations
     * @param writer the Writer used for output operations
     */
    public DuskIO(Reader reader, Writer writer) {
        if (reader == null || writer == null) {
            throw new NullPointerException("Reader and Writer cannot be null");
        }
        this.bufferedReader = new BufferedReader(reader);
        this.bufferedWriter = new BufferedWriter(writer);
    }


    /**
     * Prints one or more messages to the console, each prefixed with a tab and followed by a new line.
     *
     * @param messages an array of messages to be printed
     * @throws IOException if an I/O error occurs while writing
     */
    public void print(String... messages) throws IOException {
        for (String message : messages) {
            bufferedWriter.write("\t" + message);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush(); // Ensure output is written immediately
    }


    /**
     * Closes both the reader and writer resources associated with this instance.
     *
     * @throws IOException if an I/O error occurs while closing the resources
     */
    @Override
    public void close() throws IOException {
        bufferedReader.close();
        bufferedWriter.close();
    }
}

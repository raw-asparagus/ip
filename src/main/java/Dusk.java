import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Dusk {
    private static void printLine(BufferedWriter writer) throws IOException {
        writer.write("\t____________________________________________________________\n");
    }

    public static void main(String[] args) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))
                ) {
            printLine(writer);
            writer.write("\tHello! I'm Dusk\n" +
                    "\tAnything you want me to do for you? :D\n");
            printLine(writer);
            writer.flush();

            String input;
            while ((input = reader.readLine()) != null && !input.equals("bye")) {
                printLine(writer);
                writer.write("\t " + input + "\n");
                printLine(writer);
                writer.flush();
            }

            printLine(writer);
            writer.write("\tSee ya! Hope to see you again soon! :3\n");
            printLine(writer);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

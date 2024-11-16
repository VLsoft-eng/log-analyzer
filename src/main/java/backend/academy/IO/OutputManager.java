package backend.academy.IO;

import java.io.PrintStream;

public class OutputManager {
    PrintStream out;

    public OutputManager(PrintStream out) {
        this.out = out;
    }

    public void print(String message) {
        out.println(message);
    }
}

package backend.academy.IO;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OutputManagerTest {
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
    }

    @Test
    void printMessageTest() {
        OutputManager outputManager = new OutputManager(new PrintStream(outputStreamCaptor));
        String message = "Hello World!";

        outputManager.print(message);
        String output = outputStreamCaptor.toString().trim();

        Assertions.assertEquals(message, output);
    }
}

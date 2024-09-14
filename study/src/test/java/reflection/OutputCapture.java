package reflection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputCapture {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public void startCapture() {
        System.setOut(new PrintStream(outContent));
    }

    public void stopCapture() {
        System.setOut(originalOut);
    }

    public void assertDoseNotContain(String... unexpectedOutput) {
        assertThat(outContent.toString())
                .doesNotContain(unexpectedOutput);
    }

    public void assertContains(String... expectedOutput) {
        assertThat(outContent.toString())
                .contains(expectedOutput);
    }
}

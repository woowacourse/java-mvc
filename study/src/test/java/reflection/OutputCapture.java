package reflection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

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

    public void assertDoseNotContain(String... unexpectedOutputs) {
        assertThat(outContent.toString())
                .doesNotContain(normalizeOutputs(unexpectedOutputs));
    }

    public void assertContains(String... expectedOutputs) {
        assertThat(outContent.toString())
                .contains(normalizeOutputs(expectedOutputs));
    }

    public String[] normalizeOutputs(String... outputs) {
        return Arrays.stream(outputs)
                .map(output -> output.replace("\n", System.lineSeparator()))
                .toArray(String[]::new);
    }
}

package reflection;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;

public abstract class OutputStreamTestSetup {

    protected OutputStream out;

    @BeforeEach
    void setOutputStream() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }
}

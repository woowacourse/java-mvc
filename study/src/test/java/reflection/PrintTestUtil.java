package reflection;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AssertionsForClassTypes;

import java.io.PrintStream;

public class PrintTestUtil {

    public static AbstractStringAssert<?> assertThatOutputOf(IgnoringExceptionRunnable runnable) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));

            runnable.run();

            return AssertionsForClassTypes.assertThat(new String(outputStream.toByteArray()));
        } catch (Exception ignored) {
            return AssertionsForClassTypes.assertThat("");
        }
    }

    @FunctionalInterface
    public interface IgnoringExceptionRunnable {

        void run() throws Exception;
    }
}

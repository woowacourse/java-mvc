package reflection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class OutputTestExtension implements BeforeEachCallback, AfterEachCallback {

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.setOut(new PrintStream(this.outputStream));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        this.outputStream.reset();
        System.setOut(System.out);
    }

/*    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == OutputTestExtension.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return new OutputTestExtension();
    }

    public String getOutput() {
        return this.outputStream.toString();
    }*/
}

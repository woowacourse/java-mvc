package nextstep.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class FakeWriter extends PrintWriter {

    private StringBuilder body = new StringBuilder();

    public FakeWriter() {
        super(new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        });
    }

    @Override
    public PrintWriter append(CharSequence csq) {
        body.append(csq);
        return this;
    }

    public String getBody() {
        return body.toString();
    }
}

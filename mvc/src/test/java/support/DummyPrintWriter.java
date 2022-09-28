package support;

import java.io.PrintWriter;

public class DummyPrintWriter extends PrintWriter {

    public String written;

    public DummyPrintWriter() {
        super(System.out);
    }

    @Override
    public void write(String s) {
        super.write(s);
        written = s;
    }

    @Override
    public void close() {
    }
}

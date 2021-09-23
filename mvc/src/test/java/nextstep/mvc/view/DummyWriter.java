package nextstep.mvc.view;

import java.io.OutputStream;
import java.io.PrintWriter;

public class DummyWriter extends PrintWriter {

    private String data;

    public DummyWriter(OutputStream out) {
        super(out);
    }

    @Override
    public void print(String data) {
        this.data = data;
    }

    @Override
    public void flush() {
        return;
    }

    public String getData() {
        return data;
    }
}

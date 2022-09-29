package nextstep.mvc.view;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.util.ArrayList;
import java.util.List;

public class MockOutputStream extends ServletOutputStream {

    private final List<Integer> value = new ArrayList<>();

    @Override
    public void write(final int b) {
        value.add(b);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener writeListener) {

    }

    public String getValue() {
        final byte[] bytes = new byte[value.size()];
        for (int i = 0; i < value.size(); i++) {
            bytes[i] = value.get(i).byteValue();
        }
        return new String(bytes);
    }
}

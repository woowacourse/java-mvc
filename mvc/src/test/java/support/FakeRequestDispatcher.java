package support;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class FakeRequestDispatcher implements RequestDispatcher {

    private int forwardCount;

    public FakeRequestDispatcher() {
        this.forwardCount = 0;
    }

    @Override
    public void forward(final ServletRequest request, final ServletResponse response)
            throws ServletException, IOException {
        forwardCount += 1;
    }

    @Override
    public void include(final ServletRequest request, final ServletResponse response)
            throws ServletException, IOException {

    }

    public boolean isInvokeForward() {
        return forwardCount > 0;
    }
}

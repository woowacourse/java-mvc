package nextstep.mvc.support;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class MockRequestDispatcher implements RequestDispatcher {

    private boolean forwardExecuted;

    @Override
    public void forward(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        forwardExecuted = true;
    }

    @Override
    public void include(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        forwardExecuted = false;
    }

    public boolean isForwardExecuted() {
        return forwardExecuted;
    }
}

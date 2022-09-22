package samples;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;

public class TestManualHandlerMapping implements HandlerMapping {
    private static TestManualController CONTROLLER;

    @Override
    public void initialize() {
        CONTROLLER = new TestManualController();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return CONTROLLER;
    }
}

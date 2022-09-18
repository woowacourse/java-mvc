package samples;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;

public class TestManualHandlerMapping implements HandlerMapping {

    @Override
    public void initialize() {

    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return new TestManualController();
    }
}

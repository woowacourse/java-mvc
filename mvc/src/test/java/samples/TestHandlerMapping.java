package samples;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handlerMapping.HandlerMapping;

public class TestHandlerMapping implements HandlerMapping {
    @Override
    public void initialize() {
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return new TestController();
    }
}

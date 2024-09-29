package samples;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

public class TestHandlerMapping implements HandlerMapping {

    @Override
    public void initialize() {
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return null;
    }
}

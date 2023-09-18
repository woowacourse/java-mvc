package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapter;

public class FakeHandlerAdapter implements HandlerAdapter {

    @Override
    public Object execute(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception {
        return null;
    }

    @Override
    public boolean isSupported(final Object handler) {
        return false;
    }
}

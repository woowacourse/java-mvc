package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Controller controller = (Controller) handler;
        return controller.execute(request, response);
    }
}

package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public Object execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final Controller controller = (Controller) handler;
        return controller.execute(request, response);
    }
}

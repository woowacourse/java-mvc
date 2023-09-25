package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class NotFoundHandlerAdapter implements HandlerAdapter {

    private final String viewName;

    public NotFoundHandlerAdapter(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof NotFoundHandler;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ((NotFoundHandler) handler).handle(viewName);
    }
}

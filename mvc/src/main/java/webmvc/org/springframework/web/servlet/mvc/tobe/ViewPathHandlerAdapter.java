package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class ViewPathHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public String invoke(final Object handler, final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        return (String) ((HandlerExecution) handler).handle(request, response);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class ViewPathHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public String invoke(final Object handler, final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        return ((Controller) handler).execute(request, response);
    }
}

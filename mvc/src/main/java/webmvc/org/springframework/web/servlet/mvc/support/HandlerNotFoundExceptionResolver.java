package webmvc.org.springframework.web.servlet.mvc.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.view.JspView;

public class HandlerNotFoundExceptionResolver implements HandlerExceptionResolver {

    private final String viewPath;

    public HandlerNotFoundExceptionResolver(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Exception ex) {
        return new ModelAndView(new JspView(viewPath));
    }

    @Override
    public Class<? extends Exception> supportException() {
        return HandlerNotFoundException.class;
    }
}

package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public abstract class ExceptionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Exception;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        return handleException(response, (Exception) handler);
    }

    protected abstract ModelAndView handleException(HttpServletResponse response, Exception exception);

}

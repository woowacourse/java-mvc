package webmvc.org.springframework.web.servlet.mvc.tobe.exceptionhandlermapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface ExceptionHandler {

    void initialize();

    boolean isHandleable(final HttpServletRequest request, final HttpServletResponse response);

    ModelAndView handle(final HttpServletRequest request);
}

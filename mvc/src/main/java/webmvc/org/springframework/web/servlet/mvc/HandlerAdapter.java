package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean isSupport(final Object object);

    ModelAndView handle(final Object object, final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}

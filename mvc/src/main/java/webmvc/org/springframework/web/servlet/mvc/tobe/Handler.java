package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface Handler {

    boolean isSupport();

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}

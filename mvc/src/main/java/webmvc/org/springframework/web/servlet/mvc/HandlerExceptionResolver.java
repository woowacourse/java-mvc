package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerExceptionResolver {

    ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Exception ex) throws Exception;

    Class<? extends Exception> supportException();
}

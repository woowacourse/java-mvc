package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import webmvc.org.springframework.web.servlet.ModelAndView;

public interface ExceptionHandler {

    boolean support(Throwable ex);

    ModelAndView handle();
}

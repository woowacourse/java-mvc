package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.annotation.HandlerExecution;

public interface HandlerMapping {

    void initialize();

    boolean support(HttpServletRequest httpServletRequest);

    HandlerExecution getHandler(HttpServletRequest httpServletRequest);
}

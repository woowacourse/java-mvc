package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public interface HandlerMapping {

    void initialize();

    HandlerExecution getHandler(HttpServletRequest request) throws Exception;
}


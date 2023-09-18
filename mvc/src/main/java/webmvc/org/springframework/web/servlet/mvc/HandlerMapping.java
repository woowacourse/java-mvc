package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public interface HandlerMapping {

    HandlerExecution getHandler(HttpServletRequest request) throws Exception;
}

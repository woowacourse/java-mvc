package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public interface HandlerMapping {

    void initialize();

    Controller getHandler(HttpServletRequest request);
}

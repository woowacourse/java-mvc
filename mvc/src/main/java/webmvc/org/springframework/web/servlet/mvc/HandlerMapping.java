package webmvc.org.springframework.web.servlet.mvc;

import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public interface HandlerMapping {

    void initialize();
    Controller getHandler(final String requestURI);
}

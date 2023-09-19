package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public interface LegacyHandlerMapping {

    void initialize();

    Controller getHandler(String requestURI);

}

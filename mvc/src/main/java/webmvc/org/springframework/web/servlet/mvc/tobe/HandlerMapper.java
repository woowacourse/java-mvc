package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public interface HandlerMapper {

    void initialize();
    Controller getHandler(final String requestURI);

}

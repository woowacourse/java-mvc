package webmvc.org.springframework.web.servlet.mvc.disapatchersevlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(final HttpServletRequest request);
}

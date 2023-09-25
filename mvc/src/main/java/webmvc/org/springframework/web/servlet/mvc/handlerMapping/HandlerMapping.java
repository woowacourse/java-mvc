package webmvc.org.springframework.web.servlet.mvc.handlerMapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean canHandle(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}

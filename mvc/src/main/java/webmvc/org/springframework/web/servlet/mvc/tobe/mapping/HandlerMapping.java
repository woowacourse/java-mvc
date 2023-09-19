package webmvc.org.springframework.web.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}

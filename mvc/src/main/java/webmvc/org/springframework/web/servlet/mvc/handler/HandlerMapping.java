package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean supports(HttpServletRequest request);

    Handler getHandler(HttpServletRequest request);
}

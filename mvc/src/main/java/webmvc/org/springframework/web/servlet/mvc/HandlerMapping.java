package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean supports(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}

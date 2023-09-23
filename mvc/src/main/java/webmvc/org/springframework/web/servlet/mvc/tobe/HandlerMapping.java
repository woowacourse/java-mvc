package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean isMatch(HttpServletRequest request);

    HandlerExecution getHandler(final HttpServletRequest request);
}

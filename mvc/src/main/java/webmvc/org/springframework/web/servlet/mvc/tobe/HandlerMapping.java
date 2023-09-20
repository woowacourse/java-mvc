package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerMapping {
    void initialize();

    Object getHandler(HttpServletRequest request);

    boolean isSupport(HttpServletRequest request);

    Object executeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception;
}

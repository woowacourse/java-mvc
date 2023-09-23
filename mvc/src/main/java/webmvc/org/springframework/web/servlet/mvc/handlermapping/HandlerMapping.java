package webmvc.org.springframework.web.servlet.mvc.handlermapping;


import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initialize();

    Object getHandler(final HttpServletRequest request);
}

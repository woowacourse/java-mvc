package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    public void initialize();

    public Handler getHandler(final HttpServletRequest request);
}

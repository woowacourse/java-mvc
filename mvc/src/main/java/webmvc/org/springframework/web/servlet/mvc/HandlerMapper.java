package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapper {

    void initialize();

    Object getHandler(final HttpServletRequest request);

}

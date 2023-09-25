package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface HandlerMapping {

    Optional<Object> getHandler(final HttpServletRequest request);
}

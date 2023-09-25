package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import web.org.springframework.web.Handler;

public interface HandlerMapping {

    Optional<Handler> getHandler(final HttpServletRequest request);
}

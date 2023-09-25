package webmvc.org.springframework.web.servlet.mvc.handlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HandlerMapping {

    void initialize();

    Optional<Object> getHandler(HttpServletRequest request);
}

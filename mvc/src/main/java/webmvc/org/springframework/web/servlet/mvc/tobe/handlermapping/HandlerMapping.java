package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HandlerMapping {

    void initialize();

    Optional<Object> getHandler(final HttpServletRequest request);

}

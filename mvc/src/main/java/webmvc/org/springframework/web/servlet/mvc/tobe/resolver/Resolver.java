package webmvc.org.springframework.web.servlet.mvc.tobe.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public interface Resolver {

    Object resolveArgument(
            final Parameter parameter,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception;

    boolean isSupported(final Parameter parameter);
}

package webmvc.org.springframework.web.servlet.mvc.handlerAdaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handleradaptor {

    boolean isSame(final Object handler);

    Object execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception;
}

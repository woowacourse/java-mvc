package webmvc.org.springframework.web.servlet.mvc.supports;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.View;

public interface ViewResolver {

    boolean supports(final HttpServletRequest request, final String viewName);

    View resolve(final String viewName);
}

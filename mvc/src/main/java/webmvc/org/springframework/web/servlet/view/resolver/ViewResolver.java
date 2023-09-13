package webmvc.org.springframework.web.servlet.view.resolver;

import webmvc.org.springframework.web.servlet.View;

public interface ViewResolver {

    boolean supports(final String viewName);

    View resolve(final String viewName);
}

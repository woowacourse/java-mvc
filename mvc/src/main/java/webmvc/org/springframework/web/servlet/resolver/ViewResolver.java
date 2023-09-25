package webmvc.org.springframework.web.servlet.resolver;

import webmvc.org.springframework.web.servlet.View;

public interface ViewResolver {

    boolean supports(String viewName);

    View resolveViewName(String viewName);
}

package webmvc.org.springframework.web.servlet.view;

import webmvc.org.springframework.web.servlet.View;

public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;
}

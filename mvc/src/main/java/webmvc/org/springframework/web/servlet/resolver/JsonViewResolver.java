package webmvc.org.springframework.web.servlet.resolver;

import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JsonView;

public class JsonViewResolver implements ViewResolver{
    @Override
    public boolean supports(final String viewName) {
        return viewName == null;
    }

    @Override
    public View resolveViewName(final String viewName) {
        return new JsonView();
    }
}

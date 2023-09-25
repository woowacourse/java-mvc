package webmvc.org.springframework.web.servlet.resolver;

import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

public class JspViewResolver implements ViewResolver {

    private static final String SUFFIX = ".jsp";

    @Override
    public boolean supports(final String viewName) {
        return viewName != null && viewName.endsWith(SUFFIX);
    }

    @Override
    public View resolveViewName(final String viewName) {
        return new JspView(viewName);
    }
}

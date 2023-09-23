package webmvc.org.springframework.web.servlet.view;

import webmvc.org.springframework.web.servlet.View;

public class JspViewResolver implements ViewResolver {


    private static final String prefix = "/webapp/";
    private static final String suffix = ".jsp";

    @Override
    public View resolveViewName(final String viewName) {
        return new JspView(prefix + viewName + suffix);
    }
}

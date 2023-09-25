package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

public class JspViewResolver implements ViewResolver {

    private static final String JSP_SUFFIX = ".jsp";

    @Override
    public boolean supports(final Object view) {
        if (view instanceof String) {
            return ((String) view).endsWith(JSP_SUFFIX);
        }
        return false;
    }

    @Override
    public View resolve(final Object view) {
        return new JspView((String) view);
    }
}

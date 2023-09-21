package webmvc.org.springframework.web.servlet.mvc.view.resolver;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean supports(final HttpServletRequest ignored, final String viewName) {
        return viewName.endsWith(JspView.JSP_SUFFIX);
    }

    @Override
    public View resolve(final String viewName) {
        return new JspView(viewName);
    }
}

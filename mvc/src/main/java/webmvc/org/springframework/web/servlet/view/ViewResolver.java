package webmvc.org.springframework.web.servlet.view;

import webmvc.org.springframework.web.servlet.View;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String JSP_SUFFIX = ".jsp";

    public View resolveViewName(final String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }
        if (viewName.endsWith(JSP_SUFFIX)) {
            return new JspView(viewName);
        }
        return new JsonView();
    }
}

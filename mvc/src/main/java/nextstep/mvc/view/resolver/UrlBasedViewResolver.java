package nextstep.mvc.view.resolver;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.RedirectView;
import nextstep.mvc.view.View;

public class UrlBasedViewResolver implements ViewResolver {

    private static final String REDIRECT_URL_PREFIX = "redirect:";
    private static final String JSP_SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_URL_PREFIX.length()));
        }

        if (viewName.endsWith(JSP_SUFFIX)) {
            return new JspView(viewName);
        }

        return new JsonView();
    }
}

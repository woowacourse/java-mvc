package nextstep.mvc.viewresolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class JspViewResolver implements ViewResolver {

    private static final String JSP_SUFFIX = ".jsp";

    @Override
    public boolean supports(String viewName) {
        return viewName.endsWith(JSP_SUFFIX);
    }

    @Override
    public View resolveViewName(String viewName) {
        return new JspView(viewName);
    }
}

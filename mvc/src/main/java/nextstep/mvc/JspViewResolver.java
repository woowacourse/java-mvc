package nextstep.mvc;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class JspViewResolver implements ViewResolver {

    private static final String PREFIX = "/";
    private static final String SUFFIX = ".jsp";

    @Override
    public View resolveViewName(final String viewName) {
        return new JspView(PREFIX + viewName + SUFFIX);
    }
}

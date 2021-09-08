package nextstep.mvc.resolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.endsWith(".jsp")) {
            return new JspView(viewName);
        }
        return null;
    }
}

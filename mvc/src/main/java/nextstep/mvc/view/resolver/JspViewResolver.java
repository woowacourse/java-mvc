package nextstep.mvc.view.resolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewName;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean supports(ViewName viewName) {
        return !viewName.isEmpty() && JspView.isJspFile(viewName.value());
    }

    @Override
    public View resolve(ViewName viewName) {
        return new JspView(viewName.value());
    }
}

package nextstep.mvc.view;

import nextstep.web.support.FileType;

import static nextstep.mvc.DispatcherServlet.JSON_VIEW_NAME;
import static nextstep.mvc.view.JspView.REDIRECT_PREFIX;

public class ViewResolver {

    public View resolveViewName(String viewName) {
        if (JSON_VIEW_NAME.equals(viewName)) {
            return new JsonView();
        }
        if (FileType.matches(viewName) || viewName.startsWith(REDIRECT_PREFIX)) {
            return new JspView(viewName);
        }
        return new TextView(viewName);
    }
}

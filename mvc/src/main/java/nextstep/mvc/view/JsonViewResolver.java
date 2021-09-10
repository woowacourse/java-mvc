package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;

public class JsonViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, HttpServletRequest httpServletRequest) {
        if (viewName == null || viewName.isEmpty() || viewName.isBlank()) {
            return new JsonView();
        }
        return null;
    }
}

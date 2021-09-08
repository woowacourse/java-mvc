package nextstep.mvc.resolver;

import nextstep.mvc.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName);
}

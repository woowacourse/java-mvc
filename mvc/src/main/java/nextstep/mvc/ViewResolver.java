package nextstep.mvc;

import nextstep.mvc.view.View;

public interface ViewResolver {

    View resolveViewName(final String viewName);
}

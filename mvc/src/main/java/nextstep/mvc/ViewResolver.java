package nextstep.mvc;

import nextstep.mvc.view.View;

public interface ViewResolver {
    boolean supports(String viewName);

    View resolveViewName();
}

package nextstep.mvc.viewresolver;

import nextstep.mvc.view.View;

public interface ViewResolver {

    boolean supports(String viewName);

    View resolveViewName(String viewName);
}

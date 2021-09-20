package nextstep.mvc.view.resolver;

import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewName;

public interface ViewResolver {

    boolean supports(ViewName viewName);

    View resolve(ViewName viewName);

}

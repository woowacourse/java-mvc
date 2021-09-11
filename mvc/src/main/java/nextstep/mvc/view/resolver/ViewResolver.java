package nextstep.mvc.view.resolver;

import nextstep.mvc.view.View;

public interface ViewResolver {
    View resolve(String fileName);
}

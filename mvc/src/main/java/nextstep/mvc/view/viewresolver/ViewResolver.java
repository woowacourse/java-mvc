package nextstep.mvc.view.viewresolver;

import nextstep.mvc.view.View;

public interface ViewResolver {

    boolean support(Class<?> returnType);

    View resolve(Object object);
}

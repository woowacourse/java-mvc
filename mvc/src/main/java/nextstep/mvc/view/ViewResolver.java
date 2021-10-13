package nextstep.mvc.view;

import java.util.Locale;

public interface ViewResolver{

    boolean supports(Class<?> returnType);

    View resolve(Object object);
}

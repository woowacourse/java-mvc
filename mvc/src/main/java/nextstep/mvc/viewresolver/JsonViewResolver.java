package nextstep.mvc.viewresolver;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.View;

import java.util.Objects;

public class JsonViewResolver implements ViewResolver {

    @Override
    public boolean supports(String viewName) {
        return Objects.isNull(viewName);
    }

    @Override
    public View resolveViewName(String viewName) {
        return new JsonView();
    }
}

package nextstep.mvc.view;

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
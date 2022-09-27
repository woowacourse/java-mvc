package nextstep.mvc.view.resolver;

import java.util.Objects;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.View;

public class JsonViewResolver implements ViewResolver {

    @Override
    public View resolve(final Object view) {
        if (Objects.nonNull(view) && doesNotEndsWithJsp(view) && doesNotStartsWithRedirect(view)) {
            return new JsonView();
        }

        return null;
    }

    private boolean doesNotEndsWithJsp(final Object view) {
        if (view instanceof String && ((String) view).endsWith(".jsp")) {
            return false;
        }

        return true;
    }

    private boolean doesNotStartsWithRedirect(final Object view) {
        if (view instanceof String && ((String) view).startsWith("redirect:")) {
            return false;
        }

        return true;
    }
}

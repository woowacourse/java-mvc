package nextstep.mvc.view.resolver;

import java.util.Objects;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.View;

public class JsonViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String JSP_SUFFIX = ".jsp";

    @Override
    public View resolve(final Object view) {
        if (Objects.nonNull(view) && doesNotEndsWithJsp(view) && doesNotStartsWithRedirect(view)) {
            return new JsonView();
        }

        return null;
    }

    private boolean doesNotEndsWithJsp(final Object view) {
        if (view instanceof String && ((String) view).endsWith(JSP_SUFFIX)) {
            return false;
        }

        return true;
    }

    private boolean doesNotStartsWithRedirect(final Object view) {
        if (view instanceof String && ((String) view).startsWith(REDIRECT_PREFIX)) {
            return false;
        }

        return true;
    }
}

package nextstep.mvc.view.resolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolve(final Object view) {
        if (view instanceof String && (startsWithRedirect(view) || endsWithJsp(view))) {
            return new JspView((String) view);
        }

        return null;
    }

    private static boolean startsWithRedirect(final Object view) {
        return ((String) view).startsWith("redirect:");
    }

    private boolean endsWithJsp(final Object view) {
        return ((String) view).endsWith(".jsp");
    }
}
